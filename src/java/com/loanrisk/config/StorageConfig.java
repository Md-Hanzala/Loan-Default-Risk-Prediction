package com.loanrisk.config;

import java.util.logging.Logger;

/**
 * Manages storage mode configuration for the application.
 * Reads LOAN_STORAGE_MODE from environment variable or system property.
 * Supported valid values:
 * - 'memory': In-memory collections (for development and demonstration).
 * - 'oracle': Oracle Database JDBC repositories.
 *
 * Enforces strict validation: missing or invalid configuration produces an immediate
 * exception and never silently falls back to an alternate storage mode.
 */
public class StorageConfig {

    private static final Logger LOGGER = Logger.getLogger(StorageConfig.class.getName());

    public static final String ENV_STORAGE_MODE = "LOAN_STORAGE_MODE";
    public static final String PROP_STORAGE_MODE = "loan.storage.mode";

    private static volatile String defaultModeFromContext = null;

    public enum StorageMode {
        MEMORY("memory"),
        ORACLE("oracle");

        private final String code;

        StorageMode(String code) {
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
     * Resolves the configured StorageMode.
     * Checks System property first, then environment variable, then context configuration.
     * Throws an exception if missing or unsupported.
     *
     * @return active StorageMode (MEMORY or ORACLE)
     * @throws IllegalStateException if missing or unsupported
     */
    public static StorageMode getStorageMode() {
        String modeStr = System.getProperty(PROP_STORAGE_MODE);
        if (modeStr == null || modeStr.trim().isEmpty()) {
            modeStr = System.getenv(ENV_STORAGE_MODE);
        }
        if (modeStr == null || modeStr.trim().isEmpty()) {
            modeStr = defaultModeFromContext;
        }

        return resolveStorageMode(modeStr);
    }

    public static StorageMode resolveStorageMode(String modeStr) {
        if (modeStr == null || modeStr.trim().isEmpty()) {
            throw new IllegalStateException("LOAN_STORAGE_MODE is missing. Permitted values are strictly 'memory' or 'oracle'.");
        }

        String normalized = modeStr.trim().toLowerCase();
        if ("memory".equals(normalized)) {
            return StorageMode.MEMORY;
        } else if ("oracle".equals(normalized)) {
            return StorageMode.ORACLE;
        } else {
            throw new IllegalStateException("Unsupported LOAN_STORAGE_MODE value: '" + modeStr +
                    "'. Permitted values are strictly 'memory' or 'oracle'.");
        }
    }

    public static boolean isMemoryMode() {
        return getStorageMode() == StorageMode.MEMORY;
    }

    public static boolean isOracleMode() {
        return getStorageMode() == StorageMode.ORACLE;
    }
}
