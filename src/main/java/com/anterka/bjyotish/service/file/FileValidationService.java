package com.anterka.bjyotish.service.file;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import com.anterka.bjyotish.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Set;

@Service
@Slf4j
public class FileValidationService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");
    private static final Set<String> ALLOWED_DOCUMENT_TYPES = Set.of("application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final long MAX_DOCUMENT_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_AUDIO_SIZE = 50 * 1024 * 1024; // 50MB
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB

    /**
     * Validate file based on type and content
     */
    public void validateFile(MultipartFile file, FileTypeEnum fileType) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is required");
        }

        validateFileSize(file, fileType);
        validateFileType(file, fileType);
        validateFileContent(file, fileType);
    }

    private void validateFileSize(MultipartFile file, FileTypeEnum fileType) {
        long maxSize = switch (fileType) {
            case PROFILE_IMAGE, BIRTH_CHART_IMAGE, BLOG_FEATURED_IMAGE,
                 BLOG_CONTENT_IMAGE, PRODUCT_IMAGE, YANTRA_IMAGE -> MAX_IMAGE_SIZE;
            case CERTIFICATION, REPORT_PDF, DOCUMENT, GEMSTONE_CERTIFICATE -> MAX_DOCUMENT_SIZE;
            case AUDIO_RECORDING -> MAX_AUDIO_SIZE;
            case VIDEO_RECORDING -> MAX_VIDEO_SIZE;
            default -> MAX_DOCUMENT_SIZE;
        };

        if (file.getSize() > maxSize) {
            throw new FileUploadException(
                    String.format("File size exceeds maximum limit of %d MB for file type %s",
                            maxSize / (1024 * 1024), fileType));
        }
    }

    private void validateFileType(MultipartFile file, FileTypeEnum fileType) {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new FileUploadException("File content type cannot be determined");
        }

        boolean isValid = switch (fileType) {
            case PROFILE_IMAGE, BIRTH_CHART_IMAGE, BLOG_FEATURED_IMAGE,
                 BLOG_CONTENT_IMAGE, PRODUCT_IMAGE, YANTRA_IMAGE ->
                    ALLOWED_IMAGE_TYPES.contains(contentType);
            case CERTIFICATION, REPORT_PDF, DOCUMENT, GEMSTONE_CERTIFICATE ->
                    ALLOWED_DOCUMENT_TYPES.contains(contentType);
            case AUDIO_RECORDING -> contentType.startsWith("audio/");
            case VIDEO_RECORDING -> contentType.startsWith("video/");
            default -> false;
        };

        if (!isValid) {
            throw new FileUploadException(
                    String.format("Invalid file type %s for file type %s", contentType, fileType));
        }
    }

    private void validateFileContent(MultipartFile file, FileTypeEnum fileType) {
        try {
            // Additional content validation for images
            if (isImageFileType(fileType)) {
                validateImageContent(file);
            }
            // Additional validations for other file types can be added here
        } catch (Exception e) {
            throw new FileUploadException("File content validation failed: " + e.getMessage());
        }
    }

    private void validateImageContent(MultipartFile file) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(file.getBytes())) {
            BufferedImage image = ImageIO.read(bis);
            if (image == null) {
                throw new FileUploadException("Invalid image file - cannot be processed");
            }

            // Validate image dimensions
            if (image.getWidth() < 50 || image.getHeight() < 50) {
                throw new FileUploadException("Image dimensions too small (minimum 50x50 pixels)");
            }

            if (image.getWidth() > 5000 || image.getHeight() > 5000) {
                throw new FileUploadException("Image dimensions too large (maximum 5000x5000 pixels)");
            }
        }
    }

    private boolean isImageFileType(FileTypeEnum fileType) {
        return switch (fileType) {
            case PROFILE_IMAGE, BIRTH_CHART_IMAGE, BLOG_FEATURED_IMAGE,
                 BLOG_CONTENT_IMAGE, PRODUCT_IMAGE, YANTRA_IMAGE -> true;
            default -> false;
        };
    }
}
