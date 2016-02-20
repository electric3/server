package com.electric3.server.utils;

public class UtilityMethods {
    public static String getCurrentTimestampAsString() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
}
