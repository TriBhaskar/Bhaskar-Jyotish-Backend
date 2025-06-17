-- Bhaskar Jyotish Database Schema
-- PostgreSQL Database Design with Custom Sequences

-- ================================================================
-- SEQUENCES
-- ================================================================
CREATE SEQUENCE seq_bjyotish_user_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_user_address_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_birth_detail_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_astrologer_profile_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_astrologer_availability_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_astrologer_blocked_slot_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_consultation_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_consultation_reschedule_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_review_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_subscription_plan_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_user_subscription_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_birth_chart_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_planetary_position_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_personalized_content_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_payment_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_blog_category_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_blog_post_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_forum_category_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_forum_topic_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_forum_reply_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_product_category_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_product_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_order_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_order_item_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_notification_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_system_setting_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_audit_log_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_user_session_id
    INCREMENT 1
    START 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

-- ==============================================
-- CORE USER MANAGEMENT
-- ==============================================

-- User roles enumeration
CREATE TYPE user_role AS ENUM ('CLIENT', 'ASTROLOGER', 'ADMIN', 'MODERATOR');

-- User status enumeration
CREATE TYPE user_status AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION');

-- Main bjyotish_users table
CREATE TABLE bjyotish_users (
    id BIGINT DEFAULT nextval('seq_bjyotish_user_id') PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    role user_role NOT NULL DEFAULT 'CLIENT',
    status user_status NOT NULL DEFAULT 'PENDING_VERIFICATION',
    profile_image_url TEXT,
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    last_login_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- User addresses
CREATE TABLE user_addresses (
    id BIGINT DEFAULT nextval('seq_user_address_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id) ON DELETE CASCADE,
    address_line_1 VARCHAR(255) NOT NULL,
    address_line_2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Birth details for astrological calculations
CREATE TABLE birth_details (
    id BIGINT DEFAULT nextval('seq_birth_detail_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id) ON DELETE CASCADE,
    birth_date DATE NOT NULL,
    birth_time TIME NOT NULL,
    birth_place VARCHAR(255) NOT NULL,
    birth_latitude DECIMAL(10, 8) NOT NULL,
    birth_longitude DECIMAL(11, 8) NOT NULL,
    timezone VARCHAR(50) NOT NULL,
    is_birth_time_accurate BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- ASTROLOGER MANAGEMENT
-- ==============================================

-- Astrologer specializations
CREATE TYPE specialization_type AS ENUM (
    'CAREER', 'RELATIONSHIPS', 'HEALTH', 'FINANCE', 'MARRIAGE',
    'EDUCATION', 'BUSINESS', 'SPIRITUAL', 'REMEDIES', 'GENERAL'
);

-- Astrologer profiles
CREATE TABLE astrologer_profiles (
    id BIGINT DEFAULT nextval('seq_astrologer_profile_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id) ON DELETE CASCADE,
    display_name VARCHAR(100) NOT NULL,
    bio TEXT,
    years_of_experience INTEGER DEFAULT 0,
    languages_spoken TEXT[], -- Array of languages
    specializations specialization_type[],
    certifications TEXT[],
    consultation_fee_per_hour DECIMAL(10, 2) NOT NULL,
    minimum_consultation_duration INTEGER DEFAULT 30, -- in minutes
    is_available_for_consultation BOOLEAN DEFAULT TRUE,
    rating DECIMAL(3, 2) DEFAULT 0.00,
    total_consultations INTEGER DEFAULT 0,
    total_reviews INTEGER DEFAULT 0,
    verified_astrologer BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Astrologer availability
CREATE TABLE astrologer_availability (
    id BIGINT DEFAULT nextval('seq_astrologer_availability_id') PRIMARY KEY,
    astrologer_id BIGINT NOT NULL REFERENCES astrologer_profiles(id) ON DELETE CASCADE,
    day_of_week INTEGER NOT NULL CHECK (day_of_week >= 0 AND day_of_week <= 6), -- 0 = Sunday
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Astrologer blocked dates/times
CREATE TABLE astrologer_blocked_slots (
    id BIGINT DEFAULT nextval('seq_astrologer_blocked_slot_id') PRIMARY KEY,
    astrologer_id BIGINT NOT NULL REFERENCES astrologer_profiles(id) ON DELETE CASCADE,
    blocked_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    reason VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- CONSULTATION SYSTEM
-- ==============================================

-- Consultation status enumeration
CREATE TYPE consultation_status AS ENUM (
    'REQUESTED', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED',
    'CANCELLED', 'NO_SHOW', 'RESCHEDULED'
);

-- Consultations table
CREATE TABLE consultations (
    id BIGINT DEFAULT nextval('seq_consultation_id') PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    astrologer_id BIGINT NOT NULL REFERENCES astrologer_profiles(id),
    scheduled_date DATE NOT NULL,
    scheduled_start_time TIME NOT NULL,
    scheduled_end_time TIME NOT NULL,
    actual_start_time TIMESTAMP WITH TIME ZONE,
    actual_end_time TIMESTAMP WITH TIME ZONE,
    duration_minutes INTEGER,
    status consultation_status DEFAULT 'REQUESTED',
    consultation_fee DECIMAL(10, 2) NOT NULL,
    meeting_url VARCHAR(500),
    meeting_id VARCHAR(100),
    recording_url VARCHAR(500),
    notes TEXT,
    client_questions TEXT,
    astrologer_notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Consultation rescheduling history
CREATE TABLE consultation_reschedules (
    id BIGINT DEFAULT nextval('seq_consultation_reschedule_id') PRIMARY KEY,
    consultation_id BIGINT NOT NULL REFERENCES consultations(id) ON DELETE CASCADE,
    old_scheduled_date DATE NOT NULL,
    old_start_time TIME NOT NULL,
    old_end_time TIME NOT NULL,
    new_scheduled_date DATE NOT NULL,
    new_start_time TIME NOT NULL,
    new_end_time TIME NOT NULL,
    rescheduled_by BIGINT NOT NULL REFERENCES bjyotish_users(id),
    reason VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- REVIEWS AND RATINGS
-- ==============================================

-- Reviews table
CREATE TABLE reviews (
    id BIGINT DEFAULT nextval('seq_review_id') PRIMARY KEY,
    consultation_id BIGINT NOT NULL REFERENCES consultations(id),
    client_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    astrologer_id BIGINT NOT NULL REFERENCES astrologer_profiles(id),
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(consultation_id)
);

-- ==============================================
-- SUBSCRIPTION SYSTEM
-- ==============================================

-- Subscription plan types
CREATE TYPE subscription_plan_type AS ENUM ('MONTHLY', 'QUARTERLY', 'SEMI_ANNUAL', 'ANNUAL');

-- Subscription plans
CREATE TABLE subscription_plans (
    id BIGINT DEFAULT nextval('seq_subscription_plan_id') PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    plan_type subscription_plan_type NOT NULL,
    duration_months INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    features TEXT[],
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- User subscriptions
CREATE TYPE subscription_status AS ENUM ('ACTIVE', 'EXPIRED', 'CANCELLED', 'PAUSED');

CREATE TABLE user_subscriptions (
    id BIGINT DEFAULT nextval('seq_user_subscription_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    plan_id BIGINT NOT NULL REFERENCES subscription_plans(id),
    status subscription_status DEFAULT 'ACTIVE',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    auto_renewal BOOLEAN DEFAULT TRUE,
    payment_method VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- ASTROLOGICAL DATA
-- ==============================================

-- Birth charts
CREATE TABLE birth_charts (
    id BIGINT DEFAULT nextval('seq_birth_chart_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    birth_detail_id BIGINT NOT NULL REFERENCES birth_details(id),
    chart_data JSONB NOT NULL, -- Stores complete chart calculation results
    rashi VARCHAR(50), -- Moon sign
    lagna VARCHAR(50), -- Ascendant
    nakshatra VARCHAR(50), -- Birth star
    chart_image_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Planetary positions
CREATE TABLE planetary_positions (
    id BIGINT DEFAULT nextval('seq_planetary_position_id') PRIMARY KEY,
    birth_chart_id BIGINT NOT NULL REFERENCES birth_charts(id) ON DELETE CASCADE,
    planet VARCHAR(50) NOT NULL,
    sign VARCHAR(50) NOT NULL,
    degree DECIMAL(5, 2) NOT NULL,
    house INTEGER NOT NULL,
    retrograde BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- PERSONALIZED CONTENT
-- ==============================================

-- Content types
CREATE TYPE content_type AS ENUM (
    'DAILY_HOROSCOPE', 'WEEKLY_HOROSCOPE', 'MONTHLY_HOROSCOPE',
    'CAREER_FORECAST', 'RELATIONSHIP_ANALYSIS', 'HEALTH_INSIGHTS',
    'MUHURTA_RECOMMENDATIONS', 'TRANSIT_REPORTS', 'DASHA_ANALYSIS'
);

-- Personalized content
CREATE TABLE personalized_content (
    id BIGINT DEFAULT nextval('seq_personalized_content_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    content_type content_type NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    content_date DATE NOT NULL,
    is_sent BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- PAYMENTS
-- ==============================================

-- Payment status
CREATE TYPE payment_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED', 'CANCELLED');

-- Payment types
CREATE TYPE payment_type AS ENUM ('CONSULTATION', 'SUBSCRIPTION', 'PRODUCT_PURCHASE');

-- Payments table
CREATE TABLE payments (
    id BIGINT DEFAULT nextval('seq_payment_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    payment_type payment_type NOT NULL,
    reference_id BIGINT, -- consultation_id, subscription_id, or order_id
    payment_method VARCHAR(50),
    payment_gateway VARCHAR(50),
    gateway_transaction_id VARCHAR(255),
    gateway_payment_id VARCHAR(255),
    status payment_status DEFAULT 'PENDING',
    paid_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- KNOWLEDGE CENTER
-- ==============================================

-- Blog categories
CREATE TABLE blog_categories (
    id BIGINT DEFAULT nextval('seq_blog_category_id') PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    slug VARCHAR(100) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Blog posts
CREATE TABLE blog_posts (
    id BIGINT DEFAULT nextval('seq_blog_post_id') PRIMARY KEY,
    author_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    category_id BIGINT REFERENCES blog_categories(id),
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    content TEXT NOT NULL,
    excerpt TEXT,
    featured_image_url TEXT,
    tags TEXT[],
    is_published BOOLEAN DEFAULT FALSE,
    published_at TIMESTAMP WITH TIME ZONE,
    views_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- COMMUNITY FORUM
-- ==============================================

-- Forum categories
CREATE TABLE forum_categories (
    id BIGINT DEFAULT nextval('seq_forum_category_id') PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Forum topics
CREATE TABLE forum_topics (
    id BIGINT DEFAULT nextval('seq_forum_topic_id') PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES forum_categories(id),
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    is_pinned BOOLEAN DEFAULT FALSE,
    is_locked BOOLEAN DEFAULT FALSE,
    views_count INTEGER DEFAULT 0,
    replies_count INTEGER DEFAULT 0,
    last_reply_at TIMESTAMP WITH TIME ZONE,
    last_reply_by BIGINT REFERENCES bjyotish_users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Forum replies
CREATE TABLE forum_replies (
    id BIGINT DEFAULT nextval('seq_forum_reply_id') PRIMARY KEY,
    topic_id BIGINT NOT NULL REFERENCES forum_topics(id) ON DELETE CASCADE,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    content TEXT NOT NULL,
    parent_reply_id BIGINT REFERENCES forum_replies(id),
    is_approved BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- DIGITAL MARKETPLACE
-- ==============================================

-- Product categories
CREATE TABLE product_categories (
    id BIGINT DEFAULT nextval('seq_product_category_id') PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_category_id BIGINT REFERENCES product_categories(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Products
CREATE TABLE products (
    id BIGINT DEFAULT nextval('seq_product_id') PRIMARY KEY,
    category_id BIGINT REFERENCES product_categories(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    discount_price DECIMAL(10, 2),
    images TEXT[],
    is_digital BOOLEAN DEFAULT FALSE,
    stock_quantity INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Orders table for marketplace
CREATE TABLE orders (
    id BIGINT DEFAULT nextval('seq_order_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    order_number VARCHAR(50) UNIQUE NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2) DEFAULT 0,
    shipping_amount DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    shipping_address JSONB,
    billing_address JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Order items
CREATE TABLE order_items (
    id BIGINT DEFAULT nextval('seq_order_item_id') PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- NOTIFICATIONS
-- ==============================================

-- Notification types
CREATE TYPE notification_type AS ENUM (
    'CONSULTATION_REMINDER', 'CONSULTATION_CONFIRMED', 'CONSULTATION_CANCELLED',
    'SUBSCRIPTION_EXPIRY', 'DAILY_HOROSCOPE', 'TRANSIT_ALERT', 'SYSTEM_NOTIFICATION'
);

-- Notifications
CREATE TABLE notifications (
    id BIGINT DEFAULT nextval('seq_notification_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id),
    type notification_type NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    is_sent BOOLEAN DEFAULT FALSE,
    scheduled_at TIMESTAMP WITH TIME ZONE,
    sent_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- SYSTEM CONFIGURATION
-- ==============================================

-- System settings
CREATE TABLE system_settings (
    id BIGINT DEFAULT nextval('seq_system_setting_id') PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT,
    description TEXT,
    data_type VARCHAR(20) DEFAULT 'string',
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- AUDIT TRAIL
-- ==============================================

-- Audit log for tracking important changes
CREATE TABLE audit_logs (
    id BIGINT DEFAULT nextval('seq_audit_log_id') PRIMARY KEY,
    table_name VARCHAR(100) NOT NULL,
    record_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL, -- INSERT, UPDATE, DELETE
    old_values JSONB,
    new_values JSONB,
    changed_by BIGINT REFERENCES bjyotish_users(id),
    changed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ip_address INET,
    user_agent TEXT
);

-- ==============================================
-- SESSION MANAGEMENT
-- ==============================================

-- User sessions for authentication
CREATE TABLE user_sessions (
    id BIGINT DEFAULT nextval('seq_user_session_id') PRIMARY KEY,
    bjyotish_user_id BIGINT NOT NULL REFERENCES bjyotish_users(id) ON DELETE CASCADE,
    session_token VARCHAR(255) UNIQUE NOT NULL,
    refresh_token VARCHAR(255) UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- INDEXES FOR PERFORMANCE
-- ==============================================

-- User indexes
CREATE INDEX idx_bjyotish_users_email ON bjyotish_users(email);
CREATE INDEX idx_bjyotish_users_role ON bjyotish_users(role);
CREATE INDEX idx_bjyotish_users_status ON bjyotish_users(status);
CREATE INDEX idx_bjyotish_users_created_at ON bjyotish_users(created_at);

-- User addresses indexes
CREATE INDEX idx_user_addresses_bjyotish_user_id ON user_addresses(bjyotish_user_id);
CREATE INDEX idx_user_addresses_is_primary ON user_addresses(is_primary);

-- Birth details indexes
CREATE INDEX idx_birth_details_bjyotish_user_id ON birth_details(bjyotish_user_id);

-- Astrologer profile indexes
CREATE INDEX idx_astrologer_profiles_bjyotish_user_id ON astrologer_profiles(bjyotish_user_id);
CREATE INDEX idx_astrologer_profiles_specializations ON astrologer_profiles USING GIN(specializations);
CREATE INDEX idx_astrologer_profiles_rating ON astrologer_profiles(rating);
CREATE INDEX idx_astrologer_profiles_verified ON astrologer_profiles(verified_astrologer);

-- Astrologer availability indexes
CREATE INDEX idx_astrologer_availability_astrologer_id ON astrologer_availability(astrologer_id);
CREATE INDEX idx_astrologer_availability_day ON astrologer_availability(day_of_week);

-- Consultation indexes
CREATE INDEX idx_consultations_client_id ON consultations(client_id);
CREATE INDEX idx_consultations_astrologer_id ON consultations(astrologer_id);
CREATE INDEX idx_consultations_scheduled_date ON consultations(scheduled_date);
CREATE INDEX idx_consultations_status ON consultations(status);
CREATE INDEX idx_consultations_created_at ON consultations(created_at);

-- Review indexes
CREATE INDEX idx_reviews_astrologer_id ON reviews(astrologer_id);
CREATE INDEX idx_reviews_client_id ON reviews(client_id);
CREATE INDEX idx_reviews_rating ON reviews(rating);
CREATE INDEX idx_reviews_is_approved ON reviews(is_approved);

-- Subscription indexes
CREATE INDEX idx_user_subscriptions_bjyotish_user_id ON user_subscriptions(bjyotish_user_id);
CREATE INDEX idx_user_subscriptions_status ON user_subscriptions(status);
CREATE INDEX idx_user_subscriptions_end_date ON user_subscriptions(end_date);
CREATE INDEX idx_user_subscriptions_plan_id ON user_subscriptions(plan_id);

-- Birth chart indexes
CREATE INDEX idx_birth_charts_bjyotish_user_id ON birth_charts(bjyotish_user_id);
CREATE INDEX idx_birth_charts_birth_detail_id ON birth_charts(birth_detail_id);

-- Planetary positions indexes
CREATE INDEX idx_planetary_positions_birth_chart_id ON planetary_positions(birth_chart_id);
CREATE INDEX idx_planetary_positions_planet ON planetary_positions(planet);

-- Payment indexes
CREATE INDEX idx_payments_bjyotish_user_id ON payments(bjyotish_user_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_payment_type ON payments(payment_type);
CREATE INDEX idx_payments_reference_id ON payments(reference_id);
CREATE INDEX idx_payments_gateway_transaction_id ON payments(gateway_transaction_id);
CREATE INDEX idx_payments_created_at ON payments(created_at);

-- Content indexes
CREATE INDEX idx_personalized_content_bjyotish_user_id ON personalized_content(bjyotish_user_id);
CREATE INDEX idx_personalized_content_date ON personalized_content(content_date);
CREATE INDEX idx_personalized_content_type ON personalized_content(content_type);
CREATE INDEX idx_personalized_content_is_sent ON personalized_content(is_sent);

-- Blog indexes
CREATE INDEX idx_blog_posts_author_id ON blog_posts(author_id);
CREATE INDEX idx_blog_posts_category_id ON blog_posts(category_id);
CREATE INDEX idx_blog_posts_slug ON blog_posts(slug);
CREATE INDEX idx_blog_posts_is_published ON blog_posts(is_published);
CREATE INDEX idx_blog_posts_published_at ON blog_posts(published_at);
CREATE INDEX idx_blog_posts_tags ON blog_posts USING GIN(tags);

-- Forum indexes
CREATE INDEX idx_forum_topics_category_id ON forum_topics(category_id);
CREATE INDEX idx_forum_topics_bjyotish_user_id ON forum_topics(bjyotish_user_id);
CREATE INDEX idx_forum_topics_created_at ON forum_topics(created_at);
CREATE INDEX idx_forum_replies_topic_id ON forum_replies(topic_id);
CREATE INDEX idx_forum_replies_bjyotish_user_id ON forum_replies(bjyotish_user_id);
CREATE INDEX idx_forum_replies_parent_reply_id ON forum_replies(parent_reply_id);

-- Product indexes
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_is_active ON products(is_active);
CREATE INDEX idx_products_price ON products(price);

-- Order indexes
CREATE INDEX idx_orders_bjyotish_user_id ON orders(bjyotish_user_id);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);

-- Notification indexes
CREATE INDEX idx_notifications_bjyotish_user_id ON notifications(bjyotish_user_id);
CREATE INDEX idx_notifications_type ON notifications(type);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_is_sent ON notifications(is_sent);
CREATE INDEX idx_notifications_scheduled_at ON notifications(scheduled_at);

-- Session indexes
CREATE INDEX idx_user_sessions_bjyotish_user_id ON user_sessions(bjyotish_user_id);
CREATE INDEX idx_user_sessions_token ON user_sessions(session_token);
CREATE INDEX idx_user_sessions_refresh_token ON user_sessions(refresh_token);
CREATE INDEX idx_user_sessions_expires_at ON user_sessions(expires_at);

-- Audit log indexes
CREATE INDEX idx_audit_logs_table_name ON audit_logs(table_name);
CREATE INDEX idx_audit_logs_record_id ON audit_logs(record_id);
CREATE INDEX idx_audit_logs_changed_by ON audit_logs(changed_by);
CREATE INDEX idx_audit_logs_changed_at ON audit_logs(changed_at);

-- ==============================================
-- SEQUENCES INFORMATION
-- ==============================================

-- View all sequences created (for reference)
-- SELECT schemaname, sequencename, last_value, increment_by
-- FROM pg_sequences
-- WHERE schemaname = 'public';

-- ==============================================
-- SAMPLE DATA INSERTION
-- ==============================================

-- Insert sample subscription plans
INSERT INTO subscription_plans (name, description, plan_type, duration_months, price, features) VALUES
('Monthly Basic', 'Basic monthly plan with daily horoscope', 'MONTHLY', 1, 299.00, ARRAY['Daily Horoscope', 'Basic Birth Chart']),
('Quarterly Premium', 'Premium quarterly plan', 'QUARTERLY', 3, 799.00, ARRAY['Daily/Weekly Horoscope', 'Transit Reports', 'Muhurta Recommendations']),
('Semi-Annual Elite', 'Elite semi-annual plan', 'SEMI_ANNUAL', 6, 1499.00, ARRAY['All Premium Features', 'Monthly Consultation', 'Dasha Analysis']),
('Annual Ultimate', 'Ultimate annual plan', 'ANNUAL', 12, 2499.00, ARRAY['All Features', 'Priority Support', 'Unlimited Content']);

-- Insert sample blog categories
INSERT INTO blog_categories (name, description, slug) VALUES
('Vedic Astrology Basics', 'Fundamental concepts of Vedic astrology', 'vedic-astrology-basics'),
('Planetary Influences', 'Understanding planetary effects', 'planetary-influences'),
('Remedies & Solutions', 'Astrological remedies and solutions', 'remedies-solutions'),
('Career Guidance', 'Career-related astrological insights', 'career-guidance');

-- Insert sample forum categories
INSERT INTO forum_categories (name, description, sort_order) VALUES
('General Discussion', 'General astrology discussions', 1),
('Birth Chart Analysis', 'Discuss birth chart interpretations', 2),
('Predictions & Forecasts', 'Share and discuss predictions', 3),
('Remedies & Gemstones', 'Discuss remedies and gemstone suggestions', 4);

-- Insert sample product categories
INSERT INTO product_categories (name, description) VALUES
('Books', 'Astrological books and literature'),
('Gemstones', 'Certified gemstones and crystals'),
('Yantras', 'Sacred geometric symbols'),
('Rudraksha', 'Sacred beads for spiritual practices'),
('Consultation Reports', 'Detailed astrological reports');

-- Insert sample system settings
INSERT INTO system_settings (setting_key, setting_value, description, data_type, is_public) VALUES
('site_name', 'Bhaskar Jyotish', 'Website name', 'string', true),
('consultation_min_duration', '30', 'Minimum consultation duration in minutes', 'integer', false),
('max_reschedule_attempts', '3', 'Maximum reschedule attempts per consultation', 'integer', false),
('notification_email_enabled', 'true', 'Enable email notifications', 'boolean', false),
('notification_sms_enabled', 'true', 'Enable SMS notifications', 'boolean', false),
('default_consultation_fee', '500', 'Default consultation fee per hour', 'decimal', false),
('max_file_upload_size', '10485760', 'Maximum file upload size in bytes (10MB)', 'integer', false);

-- ==============================================
-- USEFUL FUNCTIONS
-- ==============================================

-- Function to get next available consultation slot for an astrologer
CREATE OR REPLACE FUNCTION get_next_available_slot(
    p_astrologer_id BIGINT,
    p_duration_minutes INTEGER DEFAULT 60
) RETURNS TABLE (
    available_date DATE,
    start_time TIME,
    end_time TIME
) AS $$
BEGIN
    -- This is a placeholder for the actual slot calculation logic
    -- Implementation would check astrologer availability and existing bookings
    RETURN QUERY
    SELECT
        CURRENT_DATE + 1 as available_date,
        '10:00:00'::TIME as start_time,
        ('10:00:00'::TIME + (p_duration_minutes || ' minutes')::INTERVAL)::TIME as end_time;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate astrologer rating
CREATE OR REPLACE FUNCTION update_astrologer_rating(p_astrologer_id BIGINT)
RETURNS VOID AS $$
DECLARE
    avg_rating DECIMAL(3,2);
    review_count INTEGER;
BEGIN
    SELECT AVG(rating), COUNT(*)
    INTO avg_rating, review_count
    FROM reviews
    WHERE astrologer_id = p_astrologer_id AND is_approved = TRUE;

    UPDATE astrologer_profiles
    SET rating = COALESCE(avg_rating, 0.00),
        total_reviews = review_count
    WHERE id = p_astrologer_id;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update astrologer rating when a review is added/updated
CREATE OR REPLACE FUNCTION trigger_update_astrologer_rating()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
        PERFORM update_astrologer_rating(NEW.astrologer_id);
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        PERFORM update_astrologer_rating(OLD.astrologer_id);
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_update_astrologer_rating
AFTER INSERT OR UPDATE OR DELETE ON reviews
FOR EACH ROW EXECUTE FUNCTION trigger_update_astrologer_rating();