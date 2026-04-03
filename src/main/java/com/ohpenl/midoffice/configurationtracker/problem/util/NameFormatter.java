package com.ohpenl.midoffice.configurationtracker.problem.util;

import java.util.regex.Pattern;

public class NameFormatter {
    private static final char SLASH = '/';
    private static final char DASH = '-';
    private static final Pattern SEPARATOR_CONVERTING_CHARS = Pattern.compile("[\\s_]");
    private static final Pattern INVALID_CHARS = Pattern.compile("[^a-zA-Z0-9." + SLASH + DASH + "]");
    private static final Pattern CAMEL_CASE_INSERT = Pattern.compile("(?<!\\p{Upper}|" + SLASH + "|" + DASH + ")(?=\\p{Upper})");
    private static final Pattern METHOD_REFERENCE_INSERTER = Pattern.compile("\\.-");
    private static final Pattern TRIM_PATTERN = Pattern.compile("^[/" + DASH + "]+|[/" + DASH + "]+$");

    private NameFormatter() {}

    public static String format(String value) {
        String result = SEPARATOR_CONVERTING_CHARS.matcher(value).replaceAll(String.valueOf(DASH));
        result = INVALID_CHARS.matcher(result).replaceAll("");
        result = CAMEL_CASE_INSERT.matcher(result).replaceAll(String.valueOf(DASH));
        result = METHOD_REFERENCE_INSERTER.matcher(result).replaceAll(String.valueOf(SLASH));
        result = TRIM_PATTERN.matcher(result).replaceAll("");
        return result.toLowerCase();
    }
}
