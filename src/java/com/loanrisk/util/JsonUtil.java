package com.loanrisk.util;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lightweight, zero-dependency JSON utility for formatting JSON responses
 * and parsing simple JSON request payloads.
 */
public class JsonUtil {

    /**
     * Serializes an Object (Map, List, String, Number, Boolean, null) to JSON string.
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return "\"" + escapeString((String) obj) + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof Map) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append("\"").append(escapeString(String.valueOf(entry.getKey()))).append("\":");
                sb.append(toJson(entry.getValue()));
            }
            sb.append("}");
            return sb.toString();
        }
        if (obj instanceof List) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : (List<?>) obj) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(toJson(item));
            }
            sb.append("]");
            return sb.toString();
        }
        return "\"" + escapeString(obj.toString()) + "\"";
    }

    private static String escapeString(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * Parses a simple flat JSON object string (key-value pairs) into a Map<String, String>.
     * Sufficient for reading JSON payloads submitted to the loan application endpoint.
     */
    public static Map<String, String> parseFlatJsonObject(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        if (json == null) {
            return map;
        }
        String trimmed = json.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            return map;
        }

        String content = trimmed.substring(1, trimmed.length() - 1).trim();
        if (content.isEmpty()) {
            return map;
        }

        int len = content.length();
        int i = 0;

        while (i < len) {
            // Skip whitespace
            while (i < len && Character.isWhitespace(content.charAt(i))) i++;
            if (i >= len) break;

            // Read key
            if (content.charAt(i) != '"') break;
            i++; // skip opening quote
            int keyStart = i;
            while (i < len && content.charAt(i) != '"') {
                if (content.charAt(i) == '\\' && i + 1 < len) i++;
                i++;
            }
            if (i >= len) break;
            String key = content.substring(keyStart, i);
            i++; // skip closing quote

            // Skip to colon
            while (i < len && content.charAt(i) != ':') i++;
            if (i >= len) break;
            i++; // skip colon

            // Skip whitespace
            while (i < len && Character.isWhitespace(content.charAt(i))) i++;
            if (i >= len) break;

            // Read value
            String value;
            if (content.charAt(i) == '"') {
                i++; // skip opening quote
                int valStart = i;
                while (i < len && content.charAt(i) != '"') {
                    if (content.charAt(i) == '\\' && i + 1 < len) i++;
                    i++;
                }
                value = (i < len) ? content.substring(valStart, i) : content.substring(valStart);
                if (i < len) i++; // skip closing quote
            } else {
                int valStart = i;
                while (i < len && content.charAt(i) != ',' && content.charAt(i) != '}') {
                    i++;
                }
                value = content.substring(valStart, i).trim();
                if ("null".equalsIgnoreCase(value)) {
                    value = null;
                }
            }

            map.put(key, value);

            // Skip to comma
            while (i < len && content.charAt(i) != ',') i++;
            if (i < len && content.charAt(i) == ',') {
                i++; // skip comma
            }
        }

        return map;
    }
}
