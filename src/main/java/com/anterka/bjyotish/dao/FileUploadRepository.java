package com.anterka.bjyotish.dao;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import com.anterka.bjyotish.constants.enums.UploadStatusEnum;
import com.anterka.bjyotish.entities.FileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    Optional<FileUpload> findByCloudinaryPublicId(String publicId);

    List<FileUpload> findByBjyotishUserIdAndDeletedAtIsNull(Long userId);

    List<FileUpload> findByBjyotishUserIdAndFileTypeAndDeletedAtIsNull(Long userId, FileTypeEnum fileType);

    List<FileUpload> findByEntityTypeAndEntityIdAndDeletedAtIsNull(String entityType, Long entityId);

    List<FileUpload> findByEntityTypeAndEntityIdAndFileTypeAndDeletedAtIsNull(
            String entityType, Long entityId, FileTypeEnum fileType);

    Optional<FileUpload> findByEntityTypeAndEntityIdAndIsPrimaryTrueAndDeletedAtIsNull(
            String entityType, Long entityId);

    @Query("SELECT f FROM FileUpload f WHERE f.entityType = :entityType AND f.entityId = :entityId " +
            "AND f.fileType = :fileType AND f.isPrimary = true AND f.deletedAt IS NULL AND f.uploadStatus = 'COMPLETED'")
    Optional<FileUpload> findPrimaryFileByEntity(@Param("entityType") String entityType,
                                                 @Param("entityId") Long entityId,
                                                 @Param("fileType") FileTypeEnum fileType);

    @Modifying
    @Query("UPDATE FileUpload f SET f.isPrimary = false WHERE f.entityType = :entityType " +
            "AND f.entityId = :entityId AND f.isPrimary = true AND f.deletedAt IS NULL")
    void unsetPrimaryForEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId);

    @Modifying
    @Query("UPDATE FileUpload f SET f.deletedAt = :deletedAt WHERE f.id = :fileId")
    void softDeleteFile(@Param("fileId") Long fileId, @Param("deletedAt") LocalDateTime deletedAt);

    Page<FileUpload> findByBjyotishUserIdAndUploadStatusAndDeletedAtIsNull(
            Long userId, UploadStatusEnum status, Pageable pageable);

    @Query("SELECT f FROM FileUpload f WHERE f.uploadStatus = 'FAILED' AND f.createdAt < :before")
    List<FileUpload> findFailedUploadsOlderThan(@Param("before") LocalDateTime before);
}
