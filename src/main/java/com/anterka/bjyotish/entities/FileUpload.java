package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import com.anterka.bjyotish.constants.enums.UploadStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_uploads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUpload {

    @Id
    @SequenceGenerator(name = "seq_file_upload_id", sequenceName = "seq_file_upload_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_file_upload_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileTypeEnum fileType;

    // Cloudinary specific fields
    @Column(name = "cloudinary_public_id", nullable = false, unique = true)
    private String cloudinaryPublicId;

    @Column(name = "cloudinary_secure_url", nullable = false, columnDefinition = "TEXT")
    private String cloudinarySecureUrl;

    @Column(name = "cloudinary_url", columnDefinition = "TEXT")
    private String cloudinaryUrl;

    @Column(name = "cloudinary_resource_type", length = 20)
    private String cloudinaryResourceType;

    @Column(name = "cloudinary_format", length = 10)
    private String cloudinaryFormat;

    @Column(name = "cloudinary_version")
    private Long cloudinaryVersion;

    @Column(name = "cloudinary_signature")
    private String cloudinarySignature;

    @Column(name = "cloudinary_width")
    private Integer cloudinaryWidth;

    @Column(name = "cloudinary_height")
    private Integer cloudinaryHeight;

    @Column(name = "cloudinary_bytes")
    private Long cloudinaryBytes;

    @Column(name = "cloudinary_folder")
    private String cloudinaryFolder;

    // File metadata
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "caption", columnDefinition = "TEXT")
    private String caption;

    // Reference fields
    @Column(name = "entity_type", length = 50)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    // File organization
    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    // Status and permissions
    @Enumerated(EnumType.STRING)
    @Column(name = "upload_status")
    private UploadStatusEnum uploadStatus = UploadStatusEnum.UPLOADING;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "is_approved")
    private Boolean isApproved = true;

    // Timestamps
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
