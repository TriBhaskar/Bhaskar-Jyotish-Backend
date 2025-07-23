package com.anterka.bjyotish.controller.constants;

/**
 * This holds the static API paths with the versioning as [v1]
 * Updated functionality in the APIs in any of the future release can be
 * versioned as [v2] and so on ...
 */
public class ApiPaths {
    public static final String API_CONTEXT_PATH = "/bjyotish";
    public static final String API_PREFIX = "/api";
    public static final String VERSION_V1 = "/v1";

    // Complete API base path
    public static final String API_V1_BASE = API_PREFIX + VERSION_V1;

    // ========================================
    // USER MANAGEMENT PATHS
    // ========================================
    public static final String USERS_BASE = "/users";
    public static final String AUTH_BASE = "/auth";

    // Authentication (separate from users for clarity)
    public static final String LOGIN = AUTH_BASE + "/login";
    public static final String LOGOUT = AUTH_BASE + "/logout";

    // Customer Registration
    public static final String CUSTOMER_REGISTER = AUTH_BASE + "/customer/register";
    public static final String VERIFY_EMAIL = AUTH_BASE + "/verify-email";
    public static final String VERIFY_PHONE = AUTH_BASE + "/verify-phone";
    public static final String VERIFY_OTP = AUTH_BASE + "/verify-otp";
    public static final String RESEND_OTP = AUTH_BASE + "/resend-otp";
    public static final String FORGOT_PASSWORD = AUTH_BASE + "/forgot-password";
    public static final String VALIDATE_RESET_TOKEN = AUTH_BASE + "/validate-reset-token";
    public static final String RESET_PASSWORD = AUTH_BASE + "/reset-password";

    // Astrologer Registration (separate endpoint)
    public static final String ASTROLOGER_REGISTER = AUTH_BASE + "/astrologer/register";

    // User Management
    public static final String DELETE_ACCOUNT = USERS_BASE + "/account";

    // ========================================
    // USER PROFILE ENDPOINTS
    // ========================================
    public static final String USER_PROFILE = USERS_BASE + "/profile";
    public static final String UPDATE_PROFILE = USERS_BASE + "/profile";
    public static final String UPLOAD_PROFILE_IMAGE = USERS_BASE + "/profile/image";

    // User Addresses
    public static final String USER_ADDRESSES = USERS_BASE + "/addresses";
    public static final String USER_ADDRESS_BY_ID = USERS_BASE + "/addresses/{id}";
    public static final String SET_PRIMARY_ADDRESS = USERS_BASE + "/addresses/{id}/primary";

    // Birth Details
    public static final String BIRTH_DETAILS = USERS_BASE + "/birth-details";
    public static final String BIRTH_DETAILS_BY_ID = USERS_BASE + "/birth-details/{id}";

    // ========================================
    // ASTROLOGER MANAGEMENT PATHS
    // ========================================
    public static final String ASTROLOGERS_BASE = "/astrologers";

    public static final String ASTROLOGERS = ASTROLOGERS_BASE;
    public static final String ASTROLOGER_BY_ID = ASTROLOGERS_BASE + "/{id}";
    public static final String ASTROLOGER_PROFILE = ASTROLOGERS_BASE + "/profile";
    public static final String ASTROLOGER_SEARCH = ASTROLOGERS_BASE + "/search";
    public static final String ASTROLOGER_STATUS = ASTROLOGERS_BASE + "/status";

    // Astrologer Availability
    public static final String ASTROLOGER_AVAILABILITY = ASTROLOGERS_BASE + "/{id}/availability";
    public static final String ASTROLOGER_AVAILABILITY_SLOT = ASTROLOGERS_BASE + "/availability";
    public static final String ASTROLOGER_AVAILABILITY_BY_ID = ASTROLOGERS_BASE + "/availability/{id}";

    // Astrologer Blocked Slots
    public static final String ASTROLOGER_BLOCKED_SLOTS = ASTROLOGERS_BASE + "/blocked-slots";
    public static final String ASTROLOGER_BLOCKED_SLOT_BY_ID = ASTROLOGERS_BASE + "/blocked-slots/{id}";

    // ========================================
    // CONSULTATION SYSTEM PATHS
    // ========================================
    public static final String CONSULTATIONS_BASE = "/consultations";

    public static final String CONSULTATIONS = CONSULTATIONS_BASE;
    public static final String CONSULTATION_REQUEST = CONSULTATIONS_BASE + "/request";
    public static final String CONSULTATION_BY_ID = CONSULTATIONS_BASE + "/{id}";
    public static final String CONSULTATION_CONFIRM = CONSULTATIONS_BASE + "/{id}/confirm";
    public static final String CONSULTATION_CANCEL = CONSULTATIONS_BASE + "/{id}/cancel";
    public static final String CONSULTATION_RESCHEDULE = CONSULTATIONS_BASE + "/{id}/reschedule";
    public static final String CONSULTATION_START = CONSULTATIONS_BASE + "/{id}/start";
    public static final String CONSULTATION_END = CONSULTATIONS_BASE + "/{id}/end";
    public static final String CONSULTATION_DETAILS = CONSULTATIONS_BASE + "/{id}/details";
    public static final String CONSULTATION_NOTES = CONSULTATIONS_BASE + "/{id}/notes";

    // Consultation Reschedule
    public static final String CONSULTATION_RESCHEDULE_HISTORY = CONSULTATIONS_BASE + "/{id}/reschedule-history";

    // ========================================
    // REVIEW AND RATING PATHS
    // ========================================
    public static final String REVIEWS_BASE = "/reviews";

    public static final String REVIEWS = REVIEWS_BASE;
    public static final String REVIEW_BY_ID = REVIEWS_BASE + "/{id}";
    public static final String REVIEWS_BY_ASTROLOGER = REVIEWS_BASE + "/astrologer/{astrologerId}";
    public static final String REVIEW_BY_CONSULTATION = REVIEWS_BASE + "/consultation/{consultationId}";

    // ========================================
    // SUBSCRIPTION MANAGEMENT PATHS
    // ========================================
    public static final String SUBSCRIPTIONS_BASE = "/subscriptions";

    // Subscription Plans
    public static final String SUBSCRIPTION_PLANS = SUBSCRIPTIONS_BASE + "/plans";
    public static final String SUBSCRIPTION_PLAN_BY_ID = SUBSCRIPTIONS_BASE + "/plans/{id}";

    // User Subscriptions
    public static final String USER_SUBSCRIPTION = USERS_BASE + "/subscription";
    public static final String USER_SUBSCRIPTION_SUBSCRIBE = USERS_BASE + "/subscription/subscribe";
    public static final String USER_SUBSCRIPTION_CANCEL = USERS_BASE + "/subscription/cancel";
    public static final String USER_SUBSCRIPTION_PAUSE = USERS_BASE + "/subscription/pause";
    public static final String USER_SUBSCRIPTION_RESUME = USERS_BASE + "/subscription/resume";
    public static final String USER_SUBSCRIPTION_HISTORY = USERS_BASE + "/subscription/history";

    // ========================================
    // ASTROLOGICAL DATA PATHS
    // ========================================
    public static final String BIRTH_CHARTS_BASE = "/birth-charts";

    // Birth Charts
    public static final String BIRTH_CHARTS = BIRTH_CHARTS_BASE;
    public static final String BIRTH_CHART_GENERATE = BIRTH_CHARTS_BASE + "/generate";
    public static final String BIRTH_CHART_BY_ID = BIRTH_CHARTS_BASE + "/{id}";

    // Planetary Positions
    public static final String PLANETARY_POSITIONS = BIRTH_CHARTS_BASE + "/{chartId}/planetary-positions";
    public static final String CURRENT_PLANETARY_POSITIONS = "/planetary-positions/current";

    // ========================================
    // PERSONALIZED CONTENT PATHS
    // ========================================
    public static final String CONTENT_BASE = "/content";

    public static final String USER_CONTENT = USERS_BASE + CONTENT_BASE;
    public static final String DAILY_HOROSCOPE = USERS_BASE + CONTENT_BASE + "/daily-horoscope";
    public static final String WEEKLY_HOROSCOPE = USERS_BASE + CONTENT_BASE + "/weekly-horoscope";
    public static final String MONTHLY_HOROSCOPE = USERS_BASE + CONTENT_BASE + "/monthly-horoscope";
    public static final String FORECASTS = USERS_BASE + CONTENT_BASE + "/forecasts";
    public static final String GENERATE_CONTENT = USERS_BASE + CONTENT_BASE + "/generate";

    // ========================================
    // PAYMENT PATHS
    // ========================================
    public static final String PAYMENTS_BASE = "/payments";

    public static final String PAYMENTS = PAYMENTS_BASE;
    public static final String PAYMENT_INITIATE = PAYMENTS_BASE + "/initiate";
    public static final String PAYMENT_CALLBACK = PAYMENTS_BASE + "/callback";
    public static final String PAYMENT_STATUS = PAYMENTS_BASE + "/{id}/status";
    public static final String PAYMENT_REFUND = PAYMENTS_BASE + "/{id}/refund";
    public static final String PAYMENT_HISTORY = USERS_BASE + "/payments/history";

    // ========================================
    // KNOWLEDGE CENTER PATHS
    // ========================================
    public static final String BLOG_BASE = "/blog";

    // Blog Categories
    public static final String BLOG_CATEGORIES = BLOG_BASE + "/categories";
    public static final String BLOG_CATEGORY_BY_ID = BLOG_BASE + "/categories/{id}";

    // Blog Posts
    public static final String BLOG_POSTS = BLOG_BASE + "/posts";
    public static final String BLOG_POST_BY_SLUG = BLOG_BASE + "/posts/slug/{slug}";
    public static final String BLOG_POST_BY_ID = BLOG_BASE + "/posts/{id}";
    public static final String BLOG_POST_PUBLISH = BLOG_BASE + "/posts/{id}/publish";
    public static final String BLOG_POST_VIEW = BLOG_BASE + "/posts/{id}/view";

    // ========================================
    // COMMUNITY FORUM PATHS
    // ========================================
    public static final String FORUM_BASE = "/forum";

    // Forum Categories
    public static final String FORUM_CATEGORIES = FORUM_BASE + "/categories";
    public static final String FORUM_CATEGORY_BY_ID = FORUM_BASE + "/categories/{id}";

    // Forum Topics
    public static final String FORUM_TOPICS = FORUM_BASE + "/topics";
    public static final String FORUM_TOPIC_BY_ID = FORUM_BASE + "/topics/{id}";
    public static final String FORUM_TOPIC_PIN = FORUM_BASE + "/topics/{id}/pin";
    public static final String FORUM_TOPIC_LOCK = FORUM_BASE + "/topics/{id}/lock";
    public static final String FORUM_TOPIC_VIEW = FORUM_BASE + "/topics/{id}/view";

    // Forum Replies
    public static final String FORUM_TOPIC_REPLIES = FORUM_BASE + "/topics/{topicId}/replies";
    public static final String FORUM_REPLY_BY_ID = FORUM_BASE + "/replies/{id}";

    // ========================================
    // DIGITAL MARKETPLACE PATHS
    // ========================================
    public static final String PRODUCTS_BASE = "/products";
    public static final String ORDERS_BASE = "/orders";

    // Product Categories
    public static final String PRODUCT_CATEGORIES = PRODUCTS_BASE + "/categories";
    public static final String PRODUCT_CATEGORY_BY_ID = PRODUCTS_BASE + "/categories/{id}";

    // Products
    public static final String PRODUCTS = PRODUCTS_BASE;
    public static final String PRODUCT_BY_ID = PRODUCTS_BASE + "/{id}";
    public static final String PRODUCT_SEARCH = PRODUCTS_BASE + "/search";

    // Orders
    public static final String ORDERS = ORDERS_BASE;
    public static final String ORDER_BY_ID = ORDERS_BASE + "/{id}";
    public static final String ORDER_STATUS = ORDERS_BASE + "/{id}/status";

    // Order Items
    public static final String ORDER_ITEMS = ORDERS_BASE + "/{orderId}/items";
    public static final String ORDER_ITEM_BY_ID = ORDERS_BASE + "/{orderId}/items/{itemId}";

    // ========================================
    // ADMINISTRATIVE PATHS
    // ========================================
    public static final String ADMIN_BASE = "/admin";

    public static final String ADMIN_DASHBOARD = ADMIN_BASE + "/dashboard";
    public static final String ADMIN_USERS = ADMIN_BASE + "/users";
    public static final String ADMIN_ASTROLOGER_VERIFICATION = ADMIN_BASE + "/astrologers/verification";
    public static final String ADMIN_ASTROLOGER_VERIFY = ADMIN_BASE + "/astrologers/{id}/verify";
    public static final String ADMIN_REVIEW_MODERATION = ADMIN_BASE + "/reviews/moderation";
    public static final String ADMIN_REVIEW_APPROVE = ADMIN_BASE + "/reviews/{id}/approve";

    // ========================================
    // ANALYTICS PATHS
    // ========================================
    public static final String ANALYTICS_BASE = "/analytics";

    public static final String ANALYTICS_USERS = ANALYTICS_BASE + "/users";
    public static final String ANALYTICS_CONSULTATIONS = ANALYTICS_BASE + "/consultations";
    public static final String ANALYTICS_REVENUE = ANALYTICS_BASE + "/revenue";
    public static final String ANALYTICS_ASTROLOGERS = ANALYTICS_BASE + "/astrologers";

    // ========================================
    // NOTIFICATION PATHS
    // ========================================
    public static final String NOTIFICATIONS_BASE = "/notifications";

    public static final String NOTIFICATIONS = NOTIFICATIONS_BASE;
    public static final String NOTIFICATION_BY_ID = NOTIFICATIONS_BASE + "/{id}";
    public static final String MARK_NOTIFICATION_READ = NOTIFICATIONS_BASE + "/{id}/read";
    public static final String MARK_ALL_NOTIFICATIONS_READ = NOTIFICATIONS_BASE + "/read-all";

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Get full API path with context, prefix and version
     * Example: /bjyotish/api/v1/users/profile
     */
    public static String getFullPath(String apiPath) {
        return API_CONTEXT_PATH + API_V1_BASE + apiPath;
    }

    /**
     * Get API path with version for use in @RequestMapping
     * Example: /api/v1/users/profile
     */
    public static String getVersionedPath(String path) {
        return API_V1_BASE + path;
    }

    /**
     * Get API path without version (for future v2, v3 etc.)
     * Example: /api/users/profile
     */
    public static String getApiPath(String path) {
        return API_PREFIX + path;
    }

    private ApiPaths() {}
}