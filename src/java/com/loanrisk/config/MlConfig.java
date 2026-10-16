package com.loanrisk.config;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Manages configuration for the machine learning prediction provider.
 * Supports explicit modes:
 * - 'mock': Local simulated MockPredictionProvider (for development/testing).
 * - 'http': Remote Python FastAPI inference service.
 *
 * Enforces strict validation: missing or invalid configuration produces an immediate
 * exception and never silently falls back to an alternate provider.
 */
public class MlConfig {

    private static final Logger LOGGER = Logger.getLogger(MlConfig.class.getName());

    public static final String ENV_ML_MODE = "LOAN_ML_MODE";
    public static final String PROP_ML_MODE = "loan.ml.mode";

    public static final String ENV_BASE_URL = "LOAN_ML_BASE_URL";
    public static final String PROP_BASE_URL = "loan.ml.base.url";

    public static final String ENV_CONNECT_TIMEOUT = "LOAN_ML_CONNECT_TIMEOUT_SECONDS";
    public static final String PROP_CONNECT_TIMEOUT = "loan.ml.connect.timeout.seconds";

    public static final String ENV_REQUEST_TIMEOUT = "LOAN_ML_REQUEST_TIMEOUT_SECONDS";
    public static final String PROP_REQUEST_TIMEOUT = "loan.ml.request.timeout.seconds";

    public static final int DEFAULT_CONNECT_TIMEOUT_SECONDS = 5;
    public static final int DEFAULT_REQUEST_TIMEOUT_SECONDS = 10;

    private static volatile String defaultModeFromContext = null;

    public enum MlMode {
        MOCK("mock"),
        HTTP("http");

        private final String code;

        MlMode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static void setDefaultModeFromContext(String mode) {
        defaultModeFromContext = mode;
    }

    /**
     * Resolves the configured ML mode strictly from system property, environment variable,
     * or servlet context configuration.
     *
     * @return active MlMode (MOCK or HTTP)
     * @throws IllegalStateException if mode is missing or invalid
     */
    public static MlMode getMlMode() {
        String modeStr = System.getProperty(PROP_ML_MODE);
        if (modeStr == null || modeStr.trim().isEmpty()) {
            modeStr = System.getenv(ENV_ML_MODE);
        }
        if (modeStr == null || modeStr.trim().isEmpty()) {
            modeStr = defaultModeFromContext;
        }
        return resolveMlMode(modeStr);
    }

    public static MlMode resolveMlMode(String modeStr) {
        if (modeStr == null || modeStr.trim().isEmpty()) {
            throw new IllegalStateException("LOAN_ML_MODE is missing. Permitted values are strictly 'mock' or 'http'.");
        }
        String normalized = modeStr.trim().toLowerCase();
        if ("mock".equals(normalized)) {
            return MlMode.MOCK;
        } else if ("http".equals(normalized)) {
            return MlMode.HTTP;
        } else {
            throw new IllegalStateException("Unsupported LOAN_ML_MODE value: '" + modeStr +
                    "'. Permitted values are strictly 'mock' or 'http'.");
        }
    }

    /**
     * Obtains and validates the base URL required when in HTTP mode.
     *
     * @return validated base URL string
     * @throws IllegalStateException if unset, empty, or not a valid HTTP/HTTPS URI
     */
    public static String getBaseUrl() {
        String url = System.getProperty(PROP_BASE_URL);
        if (url == null || url.trim().isEmpty()) {
            url = System.getenv(ENV_BASE_URL);
        }

        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("LOAN_ML_BASE_URL is required when LOAN_ML_MODE is 'http'.");
        }

        String trimmed = url.trim();
        try {
            URI uri = URI.create(trimmed);
            String scheme = uri.getScheme();
            if (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
                throw new IllegalStateException("Invalid LOAN_ML_BASE_URL: '" + trimmed + "'. Must specify http or https protocol.");
            }
            if (uri.getHost() == null || uri.getHost().isEmpty()) {
                throw new IllegalStateException("Invalid LOAN_ML_BASE_URL: '" + trimmed + "'. Host cannot be empty.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid LOAN_ML_BASE_URL format: '" + trimmed + "': " + e.getMessage(), e);
        }

        return trimmed;
    }

    public static int getConnectTimeoutSeconds() {
        String val = System.getProperty(PROP_CONNECT_TIMEOUT);
        if (val == null || val.trim().isEmpty()) {
            val = System.getenv(ENV_CONNECT_TIMEOUT);
        }
        if (val == null || val.trim().isEmpty()) {
            return DEFAULT_CONNECT_TIMEOUT_SECONDS;
        }
        try {
            int timeout = Integer.parseInt(val.trim());
            if (timeout <= 0) {
                throw new IllegalStateException("LOAN_ML_CONNECT_TIMEOUT_SECONDS must be a positive integer, got: " + val);
            }
            return timeout;
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid integer for LOAN_ML_CONNECT_TIMEOUT_SECONDS: '" + val + "'", e);
        }
    }

    public static int getRequestTimeoutSeconds() {
        String val = System.getProperty(PROP_REQUEST_TIMEOUT);
        if (val == null || val.trim().isEmpty()) {
            val = System.getenv(ENV_REQUEST_TIMEOUT);
        }
        if (val == null || val.trim().isEmpty()) {
            return DEFAULT_REQUEST_TIMEOUT_SECONDS;
        }
        try {
            int timeout = Integer.parseInt(val.trim());
            if (timeout <= 0) {
                throw new IllegalStateException("LOAN_ML_REQUEST_TIMEOUT_SECONDS must be a positive integer, got: " + val);
            }
            return timeout;
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid integer for LOAN_ML_REQUEST_TIMEOUT_SECONDS: '" + val + "'", e);
        }
    }
}
