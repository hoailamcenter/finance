package com.finance.utils;

import org.apache.commons.lang.RandomStringUtils;

public class GenerateUtils {
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
