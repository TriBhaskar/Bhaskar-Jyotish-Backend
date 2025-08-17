package com.anterka.bjyotish.dto.file;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {
    private MultipartFile file;
    private FileTypeEnum fileType;
    private String entityType;
    private Long entityId;
    private String altText;
    private String caption;
    private Boolean isPrimary;
    private Integer sortOrder;
    private Boolean isPublic;
}
