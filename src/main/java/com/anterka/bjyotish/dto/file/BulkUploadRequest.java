package com.anterka.bjyotish.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUploadRequest {
    private List<FileUploadRequest> files;
    private String entityType;
    private Long entityId;
}
