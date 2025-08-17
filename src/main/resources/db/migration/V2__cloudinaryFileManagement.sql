-- ==============================================
-- CLOUDINARY FILE MANAGEMENT SYSTEM
-- ==============================================

-- Create sequence for file management
CREATE SEQUENCE seq_file_upload_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

-- File types enumeration
CREATE TYPE file_type AS ENUM (
    'PROFILE_IMAGE', 'CERTIFICATION', 'CONSULTATION_NOTES', 'BIRTH_CHART_IMAGE',
    'BLOG_FEATURED_IMAGE', 'BLOG_CONTENT_IMAGE', 'PRODUCT_IMAGE',
    'FORUM_ATTACHMENT', 'REPORT_PDF', 'DOCUMENT', 'AUDIO_RECORDING',
    'VIDEO_RECORDING', 'YANTRA_IMAGE', 'GEMSTONE_CERTIFICATE'
);

-- File upload status
CREATE TYPE upload_status AS ENUM ('UPLOADING', 'COMPLETED', 'FAILED', 'DELETED');

-- Central file uploads table for all Cloudinary files
CREATE TABLE file_uploads (
    id BIGINT DEFAULT nextval('seq_file_upload_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    file_type file_type NOT NULL,

    -- Cloudinary specific fields
    cloudinary_public_id VARCHAR(255) NOT NULL UNIQUE,
    cloudinary_secure_url TEXT NOT NULL,
    cloudinary_url TEXT,
    cloudinary_resource_type VARCHAR(20) DEFAULT 'image', -- image, video, raw, auto
    cloudinary_format VARCHAR(10), -- jpg, png, pdf, etc.
    cloudinary_version BIGINT,
    cloudinary_signature VARCHAR(255),
    cloudinary_width INTEGER,
    cloudinary_height INTEGER,
    cloudinary_bytes BIGINT,
    cloudinary_folder VARCHAR(255), -- folder structure in Cloudinary

    -- File metadata
    original_filename VARCHAR(255) NOT NULL,
    file_size BIGINT, -- in bytes
    mime_type VARCHAR(100),
    alt_text VARCHAR(255), -- for accessibility
    caption TEXT,

    -- Reference fields (polymorphic associations)
    entity_type VARCHAR(50), -- 'user', 'astrologer_profile', 'consultation', 'blog_post', etc.
    entity_id BIGINT, -- ID of the related entity

    -- File organization
    is_primary BOOLEAN DEFAULT FALSE, -- primary image for entities with multiple images
    sort_order INTEGER DEFAULT 0,

    -- Status and permissions
    upload_status upload_status DEFAULT 'UPLOADING',
    is_public BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT TRUE, -- for moderation

    -- Timestamps
    uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE, -- soft delete
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for file_uploads
CREATE INDEX idx_file_uploads_user_id ON file_uploads(bjyotish_user_id);
CREATE INDEX idx_file_uploads_file_type ON file_uploads(file_type);
CREATE INDEX idx_file_uploads_cloudinary_public_id ON file_uploads(cloudinary_public_id);
CREATE INDEX idx_file_uploads_entity ON file_uploads(entity_type, entity_id);
CREATE INDEX idx_file_uploads_status ON file_uploads(upload_status);
CREATE INDEX idx_file_uploads_is_primary ON file_uploads(is_primary);
CREATE INDEX idx_file_uploads_deleted_at ON file_uploads(deleted_at);

-- ==============================================
-- UPDATE EXISTING TABLES
-- ==============================================

-- Remove direct image URL columns and replace with file references
-- Update bjyotish_users table
ALTER TABLE bjyotish_users DROP COLUMN IF EXISTS profile_image_url;

-- Update astrologer_profiles table
-- Keep consultation_fee_per_hour and other existing columns, just remove any direct file URLs
-- (No changes needed as there are no direct image URLs in astrologer_profiles)

-- Update birth_charts table
ALTER TABLE birth_charts DROP COLUMN IF EXISTS chart_image_url;

-- Update blog_posts table
ALTER TABLE blog_posts DROP COLUMN IF EXISTS featured_image_url;

-- Update products table - modify images column to reference file_uploads
ALTER TABLE products DROP COLUMN IF EXISTS images;

-- Update consultations table for meeting recordings
ALTER TABLE consultations DROP COLUMN IF EXISTS recording_url;

-- ==============================================
-- HELPER VIEWS FOR EASY FILE ACCESS
-- ==============================================

-- View for user profile images
CREATE VIEW user_profile_images AS
SELECT
    u.id as user_id,
    u.first_name,
    u.last_name,
    f.id as file_id,
    f.cloudinary_secure_url,
    f.alt_text,
    f.uploaded_at
FROM bjyotish_users u
LEFT JOIN file_uploads f ON (
    f.bjyotish_user_id = u.id
    AND f.file_type = 'PROFILE_IMAGE'
    AND f.is_primary = TRUE
    AND f.deleted_at IS NULL
    AND f.upload_status = 'COMPLETED'
);

-- View for astrologer certifications
CREATE VIEW astrologer_certifications AS
SELECT
    ap.id as astrologer_profile_id,
    ap.display_name,
    f.id as file_id,
    f.cloudinary_secure_url,
    f.original_filename,
    f.uploaded_at,
    f.is_approved
FROM astrologer_profiles ap
JOIN file_uploads f ON (
    f.entity_type = 'astrologer_profile'
    AND f.entity_id = ap.id
    AND f.file_type = 'CERTIFICATION'
    AND f.deleted_at IS NULL
    AND f.upload_status = 'COMPLETED'
)
ORDER BY ap.id, f.sort_order;

-- View for birth chart images
CREATE VIEW birth_chart_images AS
SELECT
    bc.id as birth_chart_id,
    bc.bjyotish_user_id,
    f.id as file_id,
    f.cloudinary_secure_url,
    f.uploaded_at
FROM birth_charts bc
LEFT JOIN file_uploads f ON (
    f.entity_type = 'birth_chart'
    AND f.entity_id = bc.id
    AND f.file_type = 'BIRTH_CHART_IMAGE'
    AND f.deleted_at IS NULL
    AND f.upload_status = 'COMPLETED'
);

-- View for blog post images
CREATE VIEW blog_post_images AS
SELECT
    bp.id as blog_post_id,
    bp.title,
    f.id as file_id,
    f.cloudinary_secure_url,
    f.file_type,
    f.is_primary,
    f.alt_text,
    f.caption
FROM blog_posts bp
JOIN file_uploads f ON (
    f.entity_type = 'blog_post'
    AND f.entity_id = bp.id
    AND f.file_type IN ('BLOG_FEATURED_IMAGE', 'BLOG_CONTENT_IMAGE')
    AND f.deleted_at IS NULL
    AND f.upload_status = 'COMPLETED'
)
ORDER BY bp.id, f.is_primary DESC, f.sort_order;

-- View for product images
CREATE VIEW product_images AS
SELECT
    p.id as product_id,
    p.name,
    f.id as file_id,
    f.cloudinary_secure_url,
    f.is_primary,
    f.alt_text,
    f.sort_order
FROM products p
JOIN file_uploads f ON (
    f.entity_type = 'product'
    AND f.entity_id = p.id
    AND f.file_type = 'PRODUCT_IMAGE'
    AND f.deleted_at IS NULL
    AND f.upload_status = 'COMPLETED'
)
ORDER BY p.id, f.is_primary DESC, f.sort_order;

-- ==============================================
-- UTILITY FUNCTIONS
-- ==============================================

-- Function to get primary image for any entity
CREATE OR REPLACE FUNCTION get_primary_image(
    p_entity_type VARCHAR(50),
    p_entity_id BIGINT,
    p_file_type file_type DEFAULT NULL
) RETURNS TABLE (
    file_id BIGINT,
    cloudinary_secure_url TEXT,
    alt_text VARCHAR(255)
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        f.id,
        f.cloudinary_secure_url,
        f.alt_text
    FROM file_uploads f
    WHERE f.entity_type = p_entity_type
      AND f.entity_id = p_entity_id
      AND (p_file_type IS NULL OR f.file_type = p_file_type)
      AND f.is_primary = TRUE
      AND f.deleted_at IS NULL
      AND f.upload_status = 'COMPLETED'
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

-- Function to get all images for an entity
CREATE OR REPLACE FUNCTION get_entity_files(
    p_entity_type VARCHAR(50),
    p_entity_id BIGINT,
    p_file_type file_type DEFAULT NULL
) RETURNS TABLE (
    file_id BIGINT,
    cloudinary_secure_url TEXT,
    original_filename VARCHAR(255),
    file_type file_type,
    is_primary BOOLEAN,
    sort_order INTEGER,
    uploaded_at TIMESTAMP WITH TIME ZONE
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        f.id,
        f.cloudinary_secure_url,
        f.original_filename,
        f.file_type,
        f.is_primary,
        f.sort_order,
        f.uploaded_at
    FROM file_uploads f
    WHERE f.entity_type = p_entity_type
      AND f.entity_id = p_entity_id
      AND (p_file_type IS NULL OR f.file_type = p_file_type)
      AND f.deleted_at IS NULL
      AND f.upload_status = 'COMPLETED'
    ORDER BY f.is_primary DESC, f.sort_order ASC, f.uploaded_at DESC;
END;
$$ LANGUAGE plpgsql;

-- Function to soft delete a file (marks as deleted but keeps Cloudinary reference)
CREATE OR REPLACE FUNCTION soft_delete_file(p_file_id BIGINT)
RETURNS BOOLEAN AS $$
DECLARE
    file_exists BOOLEAN;
BEGIN
    SELECT EXISTS(
        SELECT 1 FROM file_uploads
        WHERE id = p_file_id AND deleted_at IS NULL
    ) INTO file_exists;

    IF file_exists THEN
        UPDATE file_uploads
        SET deleted_at = CURRENT_TIMESTAMP,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = p_file_id;
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Function to set primary image (ensures only one primary per entity)
CREATE OR REPLACE FUNCTION set_primary_file(
    p_file_id BIGINT,
    p_entity_type VARCHAR(50),
    p_entity_id BIGINT
) RETURNS VOID AS $$
BEGIN
    -- First, unset any existing primary files for this entity
    UPDATE file_uploads
    SET is_primary = FALSE,
        updated_at = CURRENT_TIMESTAMP
    WHERE entity_type = p_entity_type
      AND entity_id = p_entity_id
      AND is_primary = TRUE
      AND deleted_at IS NULL;

    -- Set the specified file as primary
    UPDATE file_uploads
    SET is_primary = TRUE,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = p_file_id;
END;
$$ LANGUAGE plpgsql;

-- ==============================================
-- TRIGGERS FOR CLEANUP
-- ==============================================

-- Trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION trigger_update_file_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_update_file_timestamp
    BEFORE UPDATE ON file_uploads
    FOR EACH ROW EXECUTE FUNCTION trigger_update_file_timestamp();

-- ==============================================
-- SAMPLE CLOUDINARY FOLDER STRUCTURE
-- ==============================================

-- Recommended Cloudinary folder structure:
-- bhaskar_jyotish/
--   ├── users/
--   │   ├── profile_images/
--   │   └── documents/
--   ├── astrologers/
--   │   ├── profile_images/
--   │   ├── certifications/
--   │   └── consultation_notes/
--   ├── consultations/
--   │   ├── recordings/
--   │   └── notes/
--   ├── birth_charts/
--   │   └── images/
--   ├── blog/
--   │   ├── featured/
--   │   └── content/
--   ├── products/
--   │   └── images/
--   ├── reports/
--   │   └── pdfs/
--   └── forum/
--       └── attachments/

-- ==============================================
-- EXAMPLE USAGE QUERIES
-- ==============================================

-- Insert a profile image
/*
INSERT INTO file_uploads (
    bjyotish_user_id, file_type, cloudinary_public_id, cloudinary_secure_url,
    cloudinary_resource_type, cloudinary_format, original_filename,
    file_size, mime_type, entity_type, entity_id, is_primary, cloudinary_folder
) VALUES (
    1, 'PROFILE_IMAGE', 'bhaskar_jyotish/users/profile_images/user_1_profile',
    'https://res.cloudinary.com/your-cloud/image/upload/v1234567890/bhaskar_jyotish/users/profile_images/user_1_profile.jpg',
    'image', 'jpg', 'john_doe_profile.jpg',
    245760, 'image/jpeg', 'user', 1, TRUE, 'bhaskar_jyotish/users/profile_images'
);
*/

-- Insert astrologer certification
/*
INSERT INTO file_uploads (
    bjyotish_user_id, file_type, cloudinary_public_id, cloudinary_secure_url,
    cloudinary_resource_type, cloudinary_format, original_filename,
    file_size, mime_type, entity_type, entity_id, sort_order, cloudinary_folder
) VALUES (
    2, 'CERTIFICATION', 'bhaskar_jyotish/astrologers/certifications/cert_123',
    'https://res.cloudinary.com/your-cloud/image/upload/v1234567890/bhaskar_jyotish/astrologers/certifications/cert_123.pdf',
    'raw', 'pdf', 'vedic_astrology_certificate.pdf',
    1024000, 'application/pdf', 'astrologer_profile', 1, 1, 'bhaskar_jyotish/astrologers/certifications'
);
*/

-- Get user profile image
/*
SELECT * FROM user_profile_images WHERE user_id = 1;
*/

-- Get all certification files for an astrologer
/*
SELECT * FROM get_entity_files('astrologer_profile', 1, 'CERTIFICATION');
*/

-- Set a primary product image
/*
SELECT set_primary_file(123, 'product', 1);
*/