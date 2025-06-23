package com.anterka.bjyotish.controller.constants;

/**
 * This holds the static API paths with the versioning as [v1]
 * Updated functionality in the APIs in any of the future release can be
 * versioned as [v2] and so on ...
 */
public class ApiPaths {
    public static final String API_CONTEXT_PATH = "/bjyotish";
    public static final String API_PREFIX = "/api/";

    // ========================================
    // USER MANAGEMENT PATHS
    // ========================================
    public static final String LOGIN = "users/v1/login";
    public static final String REGISTER = "users/v1/register";
    public static final String USER_PROFILE = "users/v1/profile";
    public static final String VERIFY_EMAIL = "users/v1/verify-email";
    public static final String VERIFY_PHONE = "users/v1/verify-phone";
    public static final String VERIFY_OTP = "users/v1/verify-otp";
    public static final String RESEND_OTP = "users/v1/resend-otp";
    public static final String FORGOT_PASSWORD = "users/v1/forgot-password";
    public static final String VALIDATE_TOKEN = "users/v1/validate-reset-token";
    public static final String RESET_PASSWORD = "users/v1/reset-password";
    public static final String DELETE_ACCOUNT = "users/v1/account";

    // User Addresses
    public static final String USER_ADDRESSES = "users/v1/addresses";
    public static final String USER_ADDRESS_BY_ID = "users/v1/addresses/{id}";
    public static final String SET_PRIMARY_ADDRESS = "users/v1/addresses/{id}/primary";

    // Birth Details
    public static final String BIRTH_DETAILS = "users/v1/birth-details";
    public static final String BIRTH_DETAILS_BY_ID = "users/v1/birth-details/{id}";

    // ========================================
    // ASTROLOGER MANAGEMENT PATHS
    // ========================================
    public static final String ASTROLOGERS = "astrologers/v1";
    public static final String ASTROLOGER_BY_ID = "astrologers/v1/{id}";
    public static final String ASTROLOGER_PROFILE = "astrologers/v1/profile";
    public static final String ASTROLOGER_SEARCH = "astrologers/v1/search";
    public static final String ASTROLOGER_STATUS = "astrologers/v1/status";

    // Astrologer Availability
    public static final String ASTROLOGER_AVAILABILITY = "astrologers/v1/{id}/availability";
    public static final String ASTROLOGER_AVAILABILITY_SLOT = "astrologers/v1/availability";
    public static final String ASTROLOGER_AVAILABILITY_BY_ID = "astrologers/v1/availability/{id}";

    // Astrologer Blocked Slots
    public static final String ASTROLOGER_BLOCKED_SLOTS = "astrologers/v1/blocked-slots";
    public static final String ASTROLOGER_BLOCKED_SLOT_BY_ID = "astrologers/v1/blocked-slots/{id}";

    // ========================================
    // CONSULTATION SYSTEM PATHS
    // ========================================
    public static final String CONSULTATIONS = "consultations/v1";
    public static final String CONSULTATION_REQUEST = "consultations/v1/request";
    public static final String CONSULTATION_BY_ID = "consultations/v1/{id}";
    public static final String CONSULTATION_CONFIRM = "consultations/v1/{id}/confirm";
    public static final String CONSULTATION_CANCEL = "consultations/v1/{id}/cancel";
    public static final String CONSULTATION_RESCHEDULE = "consultations/v1/{id}/reschedule";
    public static final String CONSULTATION_START = "consultations/v1/{id}/start";
    public static final String CONSULTATION_END = "consultations/v1/{id}/end";
    public static final String CONSULTATION_DETAILS = "consultations/v1/{id}/details";
    public static final String CONSULTATION_NOTES = "consultations/v1/{id}/notes";

    // Consultation Reschedule
    public static final String CONSULTATION_RESCHEDULE_HISTORY = "consultations/v1/{id}/reschedule-history";

    // ========================================
    // REVIEW AND RATING PATHS
    // ========================================
    public static final String REVIEWS = "reviews/v1";
    public static final String REVIEW_BY_ID = "reviews/v1/{id}";
    public static final String REVIEWS_BY_ASTROLOGER = "reviews/v1/astrologer/{astrologerId}";
    public static final String REVIEW_BY_CONSULTATION = "reviews/v1/consultation/{consultationId}";

    // ========================================
    // SUBSCRIPTION MANAGEMENT PATHS
    // ========================================
    // Subscription Plans
    public static final String SUBSCRIPTION_PLANS = "subscription-plans/v1";
    public static final String SUBSCRIPTION_PLAN_BY_ID = "subscription-plans/v1/{id}";

    // User Subscriptions
    public static final String USER_SUBSCRIPTION = "users/v1/subscription";
    public static final String USER_SUBSCRIPTION_SUBSCRIBE = "users/v1/subscription/subscribe";
    public static final String USER_SUBSCRIPTION_CANCEL = "users/v1/subscription/cancel";
    public static final String USER_SUBSCRIPTION_PAUSE = "users/v1/subscription/pause";
    public static final String USER_SUBSCRIPTION_RESUME = "users/v1/subscription/resume";
    public static final String USER_SUBSCRIPTION_HISTORY = "users/v1/subscription/history";

    // ========================================
    // ASTROLOGICAL DATA PATHS
    // ========================================
    // Birth Charts
    public static final String BIRTH_CHARTS = "birth-charts/v1";
    public static final String BIRTH_CHART_GENERATE = "birth-charts/v1/generate";
    public static final String BIRTH_CHART_BY_ID = "birth-charts/v1/{id}";

    // Planetary Positions
    public static final String PLANETARY_POSITIONS = "birth-charts/v1/{chartId}/planetary-positions";
    public static final String CURRENT_PLANETARY_POSITIONS = "planetary-positions/v1/current";

    // ========================================
    // PERSONALIZED CONTENT PATHS
    // ========================================
    public static final String USER_CONTENT = "users/v1/content";
    public static final String DAILY_HOROSCOPE = "users/v1/content/daily-horoscope";
    public static final String WEEKLY_HOROSCOPE = "users/v1/content/weekly-horoscope";
    public static final String MONTHLY_HOROSCOPE = "users/v1/content/monthly-horoscope";
    public static final String FORECASTS = "users/v1/content/forecasts";
    public static final String GENERATE_CONTENT = "users/v1/content/generate";

    // ========================================
    // PAYMENT PATHS
    // ========================================
    public static final String PAYMENTS = "payments/v1";
    public static final String PAYMENT_INITIATE = "payments/v1/initiate";
    public static final String PAYMENT_CALLBACK = "payments/v1/callback";
    public static final String PAYMENT_STATUS = "payments/v1/{id}/status";
    public static final String PAYMENT_REFUND = "payments/v1/{id}/refund";
    public static final String PAYMENT_HISTORY = "users/v1/payments/history";

    // ========================================
    // KNOWLEDGE CENTER PATHS
    // ========================================
    // Blog Categories
    public static final String BLOG_CATEGORIES = "blog/v1/categories";
    public static final String BLOG_CATEGORY_BY_ID = "blog/v1/categories/{id}";

    // Blog Posts
    public static final String BLOG_POSTS = "blog/v1/posts";
    public static final String BLOG_POST_BY_SLUG = "blog/v1/posts/{slug}";
    public static final String BLOG_POST_BY_ID = "blog/v1/posts/{id}";
    public static final String BLOG_POST_PUBLISH = "blog/v1/posts/{id}/publish";
    public static final String BLOG_POST_VIEW = "blog/v1/posts/{id}/view";

    // ========================================
    // COMMUNITY FORUM PATHS
    // ========================================
    // Forum Categories
    public static final String FORUM_CATEGORIES = "forum/v1/categories";
    public static final String FORUM_CATEGORY_BY_ID = "forum/v1/categories/{id}";

    // Forum Topics
    public static final String FORUM_TOPICS = "forum/v1/topics";
    public static final String FORUM_TOPIC_BY_ID = "forum/v1/topics/{id}";
    public static final String FORUM_TOPIC_PIN = "forum/v1/topics/{id}/pin";
    public static final String FORUM_TOPIC_LOCK = "forum/v1/topics/{id}/lock";
    public static final String FORUM_TOPIC_VIEW = "forum/v1/topics/{id}/view";

    // Forum Replies
    public static final String FORUM_TOPIC_REPLIES = "forum/v1/topics/{topicId}/replies";
    public static final String FORUM_REPLY_BY_ID = "forum/v1/replies/{id}";

    // ========================================
    // DIGITAL MARKETPLACE PATHS
    // ========================================
    // Product Categories
    public static final String PRODUCT_CATEGORIES = "products/v1/categories";
    public static final String PRODUCT_CATEGORY_BY_ID = "products/v1/categories/{id}";

    // Products
    public static final String PRODUCTS = "products/v1";
    public static final String PRODUCT_BY_ID = "products/v1/{id}";
    public static final String PRODUCT_SEARCH = "products/v1/search";

    // Orders
    public static final String ORDERS = "orders/v1";
    public static final String ORDER_BY_ID = "orders/v1/{id}";
    public static final String ORDER_STATUS = "orders/v1/{id}/status";

    // Order Items
    public static final String ORDER_ITEMS = "orders/v1/{orderId}/items";
    public static final String ORDER_ITEM_BY_ID = "orders/v1/{orderId}/items/{itemId}";

    // ========================================
    // ADMINISTRATIVE PATHS
    // ========================================
    public static final String ADMIN_DASHBOARD = "admin/v1/dashboard";
    public static final String ADMIN_USERS = "admin/v1/users";
    public static final String ADMIN_ASTROLOGER_VERIFICATION = "admin/v1/astrologers/verification";
    public static final String ADMIN_ASTROLOGER_VERIFY = "admin/v1/astrologers/{id}/verify";
    public static final String ADMIN_REVIEW_MODERATION = "admin/v1/reviews/moderation";
    public static final String ADMIN_REVIEW_APPROVE = "admin/v1/reviews/{id}/approve";

    // ========================================
    // ANALYTICS PATHS
    // ========================================
    public static final String ANALYTICS_USERS = "analytics/v1/users";
    public static final String ANALYTICS_CONSULTATIONS = "analytics/v1/consultations";
    public static final String ANALYTICS_REVENUE = "analytics/v1/revenue";
    public static final String ANALYTICS_ASTROLOGERS = "analytics/v1/astrologers";

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Get full API path with context and prefix
     */
    public static String getFullPath(String apiPath) {
        return API_CONTEXT_PATH + API_PREFIX + apiPath;
    }

    /**
     * Get API path with just the prefix (for use in @RequestMapping)
     */
    public static String getApiPath(String path) {
        return API_PREFIX + path;
    }

    private ApiPaths() {}
}
