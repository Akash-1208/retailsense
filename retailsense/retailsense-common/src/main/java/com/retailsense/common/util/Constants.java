package com.retailsense.common.util;

public class Constants {

    // JWT
    public static final String JWT_SECRET = "${jwt.secret:mySecretKey123456789012345678901234567890}";
    public static final long JWT_EXPIRATION = 86400000; // 24 hours in milliseconds

    // Product Categories
    public static final String CATEGORY_BISCUITS = "Biscuits";
    public static final String CATEGORY_SNACKS = "Snacks";
    public static final String CATEGORY_BEVERAGES = "Beverages";
    public static final String CATEGORY_DAIRY = "Dairy";
    public static final String CATEGORY_GROCERIES = "Groceries";
    public static final String CATEGORY_PERSONAL_CARE = "Personal Care";

    // Stock Status
    public static final String STOCK_OUT = "OUT_OF_STOCK";
    public static final String STOCK_LOW = "LOW_STOCK";
    public static final String STOCK_SUFFICIENT = "SUFFICIENT";

    // AI Priority Levels
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_LOW = "LOW";

    // Date Ranges
    public static final int SALES_TREND_DAYS = 7;
    public static final int AI_ANALYSIS_DAYS = 14;

    private Constants() {
        // Private constructor to prevent instantiation
    }
}