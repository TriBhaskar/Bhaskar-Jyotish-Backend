package com.anterka.bjyotish.dto.file;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import com.anterka.bjyotish.constants.enums.UploadStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private Long fileId;
    private String cloudinaryPublicId;
    private String cloudinarySecureUrl;
    private String cloudinaryUrl;
    private String originalFilename;
    private FileTypeEnum fileType;
    private UploadStatusEnum uploadStatus;
    private Long fileSize;
    private String mimeType;
    private Integer width;
    private Integer height;
    private String altText;
    private String caption;
    private Boolean isPrimary;
    private LocalDateTime uploadedAt;
}
