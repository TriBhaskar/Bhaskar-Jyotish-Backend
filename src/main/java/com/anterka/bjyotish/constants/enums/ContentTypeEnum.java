package com.anterka.bjyotish.constants.enums;

public enum ContentTypeEnum {
    DAILY_HOROSCOPE("Daily Horoscope"),
    WEEKLY_HOROSCOPE("Weekly Horoscope"),
    MONTHLY_HOROSCOPE("Monthly Horoscope"),
    CAREER_FORECAST("Career Forecast"),
    RELATIONSHIP_ANALYSIS("Relationship Analysis"),
    HEALTH_INSIGHTS("Health Insights"),
    MUHURTA_RECOMMENDATIONS("Muhurta Recommendations"),
    TRANSIT_REPORTS("Transit Reports"),
    DASHA_ANALYSIS("Dasha Analysis");

    private final String displayName;

    ContentTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isHoroscope() {
        return this == DAILY_HOROSCOPE || this == WEEKLY_HOROSCOPE || this == MONTHLY_HOROSCOPE;
    }

    public boolean isAnalysis() {
        return this == RELATIONSHIP_ANALYSIS || this == DASHA_ANALYSIS;
    }

    public boolean isForecast() {
        return this == CAREER_FORECAST || this == TRANSIT_REPORTS;
    }

    public boolean isRecommendation() {
        return this == MUHURTA_RECOMMENDATIONS;
    }

    public boolean isHealthRelated() {
        return this == HEALTH_INSIGHTS;
    }

    public String getCategory() {
        if (isHoroscope()) return "Horoscope";
        if (isAnalysis()) return "Analysis";
        if (isForecast()) return "Forecast";
        if (isRecommendation()) return "Recommendation";
        if (isHealthRelated()) return "Health";
        return "General";
    }
}

