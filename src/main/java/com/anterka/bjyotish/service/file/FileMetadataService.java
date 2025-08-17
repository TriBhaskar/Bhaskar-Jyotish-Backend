package com.anterka.bjyotish.service.file;

import com.anterka.bjyotish.dao.FileUploadRepository;
import com.anterka.bjyotish.dto.file.FileMetadataResponse;
import com.anterka.bjyotish.entities.FileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileMetadataService {

    private final FileUploadRepository fileUploadRepository;

    /**
     * Get file metadata by ID
     */
    public Optional<FileMetadataResponse> getFileMetadata(Long fileId) {
        return fileUploadRepository.findById(fileId)
                .filter(file -> file.getDeletedAt() == null)
                .map(this::mapToMetadataResponse);
    }

    /**
     * Get all file metadata for a user
     */
    public List<FileMetadataResponse> getUserFileMetadata(Long userId) {
        return fileUploadRepository.findByBjyotishUserIdAndDeletedAtIsNull(userId)
                .stream()
                .map(this::mapToMetadataResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get file statistics for a user
     */
    public Map<String, Object> getUserFileStatistics(Long userId) {
        List<FileUpload> userFiles = fileUploadRepository.findByBjyotishUserIdAndDeletedAtIsNull(userId);

        long totalFiles = userFiles.size();
        long totalSize = userFiles.stream().mapToLong(f -> f.getCloudinaryBytes() != null ? f.getCloudinaryBytes() : 0).sum();

        Map<String, Long> fileTypeCount = userFiles.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getFileType().name(),
                        Collectors.counting()
                ));

        return Map.of(
                "totalFiles", totalFiles,
                "totalSizeBytes", totalSize,
                "totalSizeMB", totalSize / (1024 * 1024),
                "fileTypeDistribution", fileTypeCount
        );
    }

    private FileMetadataResponse mapToMetadataResponse(FileUpload fileUpload) {
        return FileMetadataResponse.builder()
                .fileId(fileUpload.getId())
                .originalFilename(fileUpload.getOriginalFilename())
                .fileType(fileUpload.getFileType())
                .fileSize(fileUpload.getCloudinaryBytes())
                .mimeType(fileUpload.getMimeType())
                .width(fileUpload.getCloudinaryWidth())
                .height(fileUpload.getCloudinaryHeight())
                .uploadedAt(fileUpload.getUploadedAt())
                .isPrimary(fileUpload.getIsPrimary())
                .isPublic(fileUpload.getIsPublic())
                .entityType(fileUpload.getEntityType())
                .entityId(fileUpload.getEntityId())
                .build();
    }
}

