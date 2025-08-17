package com.anterka.bjyotish.controller.fileuploadcontrollers;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import com.anterka.bjyotish.dto.file.BulkUploadRequest;
import com.anterka.bjyotish.dto.file.BulkUploadResponse;
import com.anterka.bjyotish.dto.file.FileUploadRequest;
import com.anterka.bjyotish.dto.file.FileUploadResponse;
import com.anterka.bjyotish.entities.BjyotishUser;
import com.anterka.bjyotish.service.BjyotishUserService;
import com.anterka.bjyotish.service.file.cloud.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileUploadController {

    private final CloudinaryService cloudinaryService;
    private final BjyotishUserService bjyotishUserService;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") FileTypeEnum fileType,
            @RequestParam(value = "entityType", required = false) String entityType,
            @RequestParam(value = "entityId", required = false) Long entityId,
            @RequestParam(value = "altText", required = false) String altText,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "isPrimary", required = false, defaultValue = "false") Boolean isPrimary,
            @RequestParam(value = "isPublic", required = false, defaultValue = "false") Boolean isPublic,
            Authentication authentication) throws FileUploadException {

        BjyotishUser user = (BjyotishUser) bjyotishUserService.loadUserByUsername(authentication.getName());

        FileUploadRequest request = FileUploadRequest.builder()
                .file(file)
                .fileType(fileType)
                .entityType(entityType)
                .entityId(entityId)
                .altText(altText)
                .caption(caption)
                .isPrimary(isPrimary)
                .isPublic(isPublic)
                .build();

        FileUploadResponse response = cloudinaryService.uploadFile(request, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload/bulk")
    public ResponseEntity<BulkUploadResponse> uploadBulkFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("fileType") FileTypeEnum fileType,
            @RequestParam(value = "entityType", required = false) String entityType,
            @RequestParam(value = "entityId", required = false) Long entityId,
            Authentication authentication) {

        BjyotishUser user = (BjyotishUser) bjyotishUserService.loadUserByUsername(authentication.getName());

        List<FileUploadRequest> fileRequests = files.stream()
                .map(file -> FileUploadRequest.builder()
                        .file(file)
                        .fileType(fileType)
                        .entityType(entityType)
                        .entityId(entityId)
                        .build())
                .toList();

        BulkUploadRequest request = BulkUploadRequest.builder()
                .files(fileRequests)
                .entityType(entityType)
                .entityId(entityId)
                .build();

        BulkUploadResponse response = cloudinaryService.uploadBulkFiles(request, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<FileUploadResponse>> getFilesByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId,
            @RequestParam(value = "fileType", required = false) FileTypeEnum fileType) {

        List<FileUploadResponse> files = cloudinaryService.getFilesByEntity(entityType, entityId, fileType);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/entity/{entityType}/{entityId}/primary")
    public ResponseEntity<FileUploadResponse> getPrimaryFile(
            @PathVariable String entityType,
            @PathVariable Long entityId,
            @RequestParam(value = "fileType", required = false) FileTypeEnum fileType) {

        Optional<FileUploadResponse> file = cloudinaryService.getPrimaryFile(entityType, entityId, fileType);
        return file.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{fileId}/set-primary")
    public ResponseEntity<Void> setPrimaryFile(@PathVariable Long fileId) throws FileUploadException {
        cloudinaryService.setPrimaryFile(fileId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long fileId,
            @RequestParam(value = "hardDelete", defaultValue = "false") boolean hardDelete) throws FileUploadException {

        cloudinaryService.deleteFile(fileId, hardDelete);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{fileId}/thumbnail")
    public ResponseEntity<Map<String, String>> getThumbnail(
            @PathVariable Long fileId,
            @RequestParam(value = "width", defaultValue = "150") int width,
            @RequestParam(value = "height", defaultValue = "150") int height) {

        // Implementation would get the file's public ID and generate thumbnail URL
        return ResponseEntity.ok(Map.of("thumbnailUrl", "thumbnail_url_here"));
    }
}
