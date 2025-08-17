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
public class BulkUploadResponse {
    private List<FileUploadResponse> successfulUploads;
    private List<String> failedUploads;
    private int totalFiles;
    private int successCount;
    private int failureCount;
}
