package com.anterka.bjyotish.service.file.cloud;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import com.anterka.bjyotish.constants.enums.UploadStatusEnum;
import com.anterka.bjyotish.dao.FileUploadRepository;
import com.anterka.bjyotish.dto.file.BulkUploadRequest;
import com.anterka.bjyotish.dto.file.BulkUploadResponse;
import com.anterka.bjyotish.dto.file.FileUploadRequest;
import com.anterka.bjyotish.dto.file.FileUploadResponse;
import com.anterka.bjyotish.entities.BjyotishUser;
import com.anterka.bjyotish.entities.FileUpload;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final FileUploadRepository fileUploadRepository;

    private static final String BASE_FOLDER = "bhaskar_jyotish";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");
    private static final Set<String> ALLOWED_DOCUMENT_TYPES = Set.of("application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    private static final Set<String> ALLOWED_AUDIO_TYPES = Set.of("audio/mpeg", "audio/wav", "audio/ogg");
    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of("video/mp4", "video/avi", "video/quicktime");

    /**
     * Upload single file to Cloudinary
     */
    @Transactional
    public FileUploadResponse uploadFile(FileUploadRequest request, BjyotishUser user) throws FileUploadException {
        validateFileUploadRequest(request);

        try {
            // Create initial database record
            FileUpload fileUpload = createInitialFileRecord(request, user);
            fileUpload = fileUploadRepository.save(fileUpload);

            // Upload to Cloudinary
            Map<String, Object> uploadResult = performCloudinaryUpload(request.getFile(), request.getFileType(), user.getId());

            // Update database record with Cloudinary response
            updateFileUploadWithCloudinaryData(fileUpload, uploadResult);
            fileUpload.setUploadStatus(UploadStatusEnum.COMPLETED);
            fileUpload.setUploadedAt(LocalDateTime.now());

            // Handle primary file setting
            if (Boolean.TRUE.equals(request.getIsPrimary())) {
                setPrimaryFile(fileUpload);
            }

            fileUpload = fileUploadRepository.save(fileUpload);

            log.info("File uploaded successfully: {}", fileUpload.getCloudinaryPublicId());
            return mapToFileUploadResponse(fileUpload);

        } catch (Exception e) {
            log.error("File upload failed for user: {}, file: {}", user.getId(), request.getFile().getOriginalFilename(), e);
            throw new FileUploadException("File upload failed: " + e.getMessage());
        }
    }

    /**
     * Upload multiple files in bulk
     */
    @Transactional
    public BulkUploadResponse uploadBulkFiles(BulkUploadRequest request, BjyotishUser user) {
        List<CompletableFuture<FileUploadResponse>> uploadFutures = request.getFiles().stream()
                .map(fileRequest -> {
                    fileRequest.setEntityType(request.getEntityType());
                    fileRequest.setEntityId(request.getEntityId());
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            return uploadFile(fileRequest, user);
                        } catch (Exception e) {
                            log.error("Bulk upload failed for file: {}", fileRequest.getFile().getOriginalFilename(), e);
                            return null;
                        }
                    });
                })
                .collect(Collectors.toList());

        List<FileUploadResponse> successfulUploads = new ArrayList<>();
        List<String> failedUploads = new ArrayList<>();

        for (int i = 0; i < uploadFutures.size(); i++) {
            try {
                FileUploadResponse response = uploadFutures.get(i).get();
                if (response != null) {
                    successfulUploads.add(response);
                } else {
                    failedUploads.add(request.getFiles().get(i).getFile().getOriginalFilename());
                }
            } catch (Exception e) {
                failedUploads.add(request.getFiles().get(i).getFile().getOriginalFilename());
            }
        }

        return BulkUploadResponse.builder()
                .successfulUploads(successfulUploads)
                .failedUploads(failedUploads)
                .totalFiles(request.getFiles().size())
                .successCount(successfulUploads.size())
                .failureCount(failedUploads.size())
                .build();
    }

    /**
     * Get files by entity
     */
    public List<FileUploadResponse> getFilesByEntity(String entityType, Long entityId, FileTypeEnum fileType) {
        List<FileUpload> files;
        if (fileType != null) {
            files = fileUploadRepository.findByEntityTypeAndEntityIdAndFileTypeAndDeletedAtIsNull(entityType, entityId, fileType);
        } else {
            files = fileUploadRepository.findByEntityTypeAndEntityIdAndDeletedAtIsNull(entityType, entityId);
        }

        return files.stream()
                .filter(file -> file.getUploadStatus() == UploadStatusEnum.COMPLETED)
                .map(this::mapToFileUploadResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get primary file for entity
     */
    public Optional<FileUploadResponse> getPrimaryFile(String entityType, Long entityId, FileTypeEnum fileType) {
        return fileUploadRepository.findPrimaryFileByEntity(entityType, entityId, fileType)
                .map(this::mapToFileUploadResponse);
    }

    /**
     * Set file as primary
     */
    @Transactional
    public void setPrimaryFile(Long fileId) throws FileUploadException {
        FileUpload fileUpload = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new FileUploadException("File not found: " + fileId));

        setPrimaryFile(fileUpload);
        fileUploadRepository.save(fileUpload);
    }

    /**
     * Delete file (soft delete)
     */
    @Transactional
    public void deleteFile(Long fileId, boolean hardDelete) throws FileUploadException {
        FileUpload fileUpload = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new FileUploadException("File not found: " + fileId));

        if (hardDelete) {
            // Delete from Cloudinary
            try {
                cloudinary.uploader().destroy(fileUpload.getCloudinaryPublicId(), ObjectUtils.emptyMap());
                log.info("File deleted from Cloudinary: {}", fileUpload.getCloudinaryPublicId());
            } catch (Exception e) {
                log.error("Failed to delete file from Cloudinary: {}", fileUpload.getCloudinaryPublicId(), e);
            }

            // Delete from database
            fileUploadRepository.delete(fileUpload);
        } else {
            // Soft delete
            fileUpload.setDeletedAt(LocalDateTime.now());
            fileUpload.setUploadStatus(UploadStatusEnum.DELETED);
            fileUploadRepository.save(fileUpload);
        }
    }

    /**
     * Get optimized image URL with transformations
     */
    public String getOptimizedImageUrl(String publicId, Map<String, Object> transformationsMap) {
        try {
            Transformation transformation = new Transformation();
            transformationsMap.forEach(transformation::param);

            return cloudinary.url()
                    .transformation(transformation)
                    .secure(true)
                    .generate(publicId);
        } catch (Exception e) {
            log.error("Failed to generate optimized URL for: {}", publicId, e);
            return null;
        }
    }

    /**
     * Generate thumbnail URL
     */
    public String getThumbnailUrl(String publicId, int width, int height) {
        Map<String, Object> transformation = ObjectUtils.asMap(
                "width", width,
                "height", height,
                "crop", "fill",
                "quality", "auto",
                "format", "auto"
        );
        return getOptimizedImageUrl(publicId, transformation);
    }

    // Private helper methods

    private void validateFileUploadRequest(FileUploadRequest request) throws FileUploadException {
        MultipartFile file = request.getFile();

        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is required");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException("File size exceeds maximum limit of " + (MAX_FILE_SIZE / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        FileTypeEnum fileType = request.getFileType();

        validateFileTypeAndContent(contentType, fileType);
    }

    private void validateFileTypeAndContent(String contentType, FileTypeEnum fileType) throws FileUploadException {
        switch (fileType) {
            case PROFILE_IMAGE, BIRTH_CHART_IMAGE, BLOG_FEATURED_IMAGE, BLOG_CONTENT_IMAGE,
                 PRODUCT_IMAGE, YANTRA_IMAGE -> {
                if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
                    throw new FileUploadException("Invalid image format. Allowed: JPEG, PNG, GIF, WebP");
                }
            }
            case CERTIFICATION, REPORT_PDF, DOCUMENT -> {
                if (!ALLOWED_DOCUMENT_TYPES.contains(contentType)) {
                    throw new FileUploadException("Invalid document format. Allowed: PDF, DOC, DOCX");
                }
            }
            case AUDIO_RECORDING -> {
                if (!ALLOWED_AUDIO_TYPES.contains(contentType)) {
                    throw new FileUploadException("Invalid audio format. Allowed: MP3, WAV, OGG");
                }
            }
            case VIDEO_RECORDING -> {
                if (!ALLOWED_VIDEO_TYPES.contains(contentType)) {
                    throw new FileUploadException("Invalid video format. Allowed: MP4, AVI, QuickTime");
                }
            }
        }
    }

    private FileUpload createInitialFileRecord(FileUploadRequest request, BjyotishUser user) {
        return FileUpload.builder()
                .bjyotishUser(user)
                .fileType(request.getFileType())
                .originalFilename(request.getFile().getOriginalFilename())
                .fileSize(request.getFile().getSize())
                .mimeType(request.getFile().getContentType())
                .altText(request.getAltText())
                .caption(request.getCaption())
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .isPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .uploadStatus(UploadStatusEnum.UPLOADING)
                .build();
    }

    private Map<String, Object> performCloudinaryUpload(MultipartFile file, FileTypeEnum fileType, Long userId) throws IOException, IOException {
        String folder = generateCloudinaryFolder(fileType, userId);
        String publicId = generatePublicId(fileType, userId, file.getOriginalFilename());

        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", folder,
                "public_id", publicId,
                "resource_type", getResourceType(file.getContentType()),
                "use_filename", true,
                "unique_filename", false,
                "overwrite", false
        );

        // Add specific params based on file type
        if (isImageType(file.getContentType())) {
            uploadParams.put("quality", "auto");
            uploadParams.put("fetch_format", "auto");
        }

        return cloudinary.uploader().upload(file.getBytes(), uploadParams);
    }

    private void updateFileUploadWithCloudinaryData(FileUpload fileUpload, Map<String, Object> uploadResult) {
        fileUpload.setCloudinaryPublicId((String) uploadResult.get("public_id"));
        fileUpload.setCloudinarySecureUrl((String) uploadResult.get("secure_url"));
        fileUpload.setCloudinaryUrl((String) uploadResult.get("url"));
        fileUpload.setCloudinaryResourceType((String) uploadResult.get("resource_type"));
        fileUpload.setCloudinaryFormat((String) uploadResult.get("format"));
        fileUpload.setCloudinaryVersion(((Number) uploadResult.get("version")).longValue());
        fileUpload.setCloudinarySignature((String) uploadResult.get("signature"));
        fileUpload.setCloudinaryBytes(((Number) uploadResult.get("bytes")).longValue());

        if (uploadResult.get("width") != null) {
            fileUpload.setCloudinaryWidth(((Number) uploadResult.get("width")).intValue());
        }
        if (uploadResult.get("height") != null) {
            fileUpload.setCloudinaryHeight(((Number) uploadResult.get("height")).intValue());
        }

        fileUpload.setCloudinaryFolder((String) uploadResult.get("folder"));
    }

    private void setPrimaryFile(FileUpload fileUpload) {
        if (fileUpload.getEntityType() != null && fileUpload.getEntityId() != null) {
            // Unset existing primary files for this entity
            fileUploadRepository.unsetPrimaryForEntity(fileUpload.getEntityType(), fileUpload.getEntityId());
            fileUpload.setIsPrimary(true);
        }
    }

    private String generateCloudinaryFolder(FileTypeEnum fileType, Long userId) {
        return switch (fileType) {
            case PROFILE_IMAGE -> BASE_FOLDER + "/users/profile_images";
            case CERTIFICATION -> BASE_FOLDER + "/astrologers/certifications";
            case CONSULTATION_NOTES -> BASE_FOLDER + "/consultations/notes";
            case BIRTH_CHART_IMAGE -> BASE_FOLDER + "/birth_charts/images";
            case BLOG_FEATURED_IMAGE -> BASE_FOLDER + "/blog/featured";
            case BLOG_CONTENT_IMAGE -> BASE_FOLDER + "/blog/content";
            case PRODUCT_IMAGE -> BASE_FOLDER + "/products/images";
            case FORUM_ATTACHMENT -> BASE_FOLDER + "/forum/attachments";
            case REPORT_PDF -> BASE_FOLDER + "/reports/pdfs";
            case DOCUMENT -> BASE_FOLDER + "/documents";
            case AUDIO_RECORDING -> BASE_FOLDER + "/consultations/audio";
            case VIDEO_RECORDING -> BASE_FOLDER + "/consultations/video";
            case YANTRA_IMAGE -> BASE_FOLDER + "/products/yantras";
            case GEMSTONE_CERTIFICATE -> BASE_FOLDER + "/products/certificates";
        };
    }

    private String generatePublicId(FileTypeEnum fileType, Long userId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String cleanFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        return fileType.name().toLowerCase() + "_" + userId + "_" + timestamp + "_" + cleanFilename;
    }

    private String getResourceType(String mimeType) {
        if (isImageType(mimeType)) return "image";
        if (isVideoType(mimeType)) return "video";
        return "raw"; // for documents, audio, etc.
    }

    private boolean isImageType(String mimeType) {
        return ALLOWED_IMAGE_TYPES.contains(mimeType);
    }

    private boolean isVideoType(String mimeType) {
        return ALLOWED_VIDEO_TYPES.contains(mimeType);
    }

    private FileUploadResponse mapToFileUploadResponse(FileUpload fileUpload) {
        return FileUploadResponse.builder()
                .fileId(fileUpload.getId())
                .cloudinaryPublicId(fileUpload.getCloudinaryPublicId())
                .cloudinarySecureUrl(fileUpload.getCloudinarySecureUrl())
                .cloudinaryUrl(fileUpload.getCloudinaryUrl())
                .originalFilename(fileUpload.getOriginalFilename())
                .fileType(fileUpload.getFileType())
                .uploadStatus(fileUpload.getUploadStatus())
                .fileSize(fileUpload.getCloudinaryBytes())
                .mimeType(fileUpload.getMimeType())
                .width(fileUpload.getCloudinaryWidth())
                .height(fileUpload.getCloudinaryHeight())
                .altText(fileUpload.getAltText())
                .caption(fileUpload.getCaption())
                .isPrimary(fileUpload.getIsPrimary())
                .uploadedAt(fileUpload.getUploadedAt())
                .build();
    }
}
