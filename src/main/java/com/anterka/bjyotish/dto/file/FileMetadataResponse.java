package com.anterka.bjyotish.dto.file;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataResponse {
    private Long fileId;
    private String originalFilename;
    private FileTypeEnum fileType;
    private Long fileSize;
    private String mimeType;
    private Integer width;
    private Integer height;
    private LocalDateTime uploadedAt;
    private Boolean isPrimary;
    private Boolean isPublic;
    private String entityType;
    private Long entityId;
}