package com.loanrisk.test;

import com.loanrisk.config.MlConfig;
import com.loanrisk.config.MlConfig.MlMode;
import com.loanrisk.config.StorageConfig;
import com.loanrisk.config.StorageConfig.StorageMode;

/**
 * Automated test suite for Configuration and Hardening requirements.
 */
public class ConfigurationTest {

    public static void runAll() {
        System.out.println("--- Running Configuration Tests ---");
        testValidStorageModes();
        testInvalidAndMissingStorageModes();
        testValidMlModes();
        testInvalidAndMissingMlModes();
        testMissingHttpBaseUrl();
        testMalformedTimeoutValues();
        testNoSilentFallbackOnConfigErrors();
    }

    private static void testValidStorageModes() {
        StorageMode m1 = StorageConfig.resolveStorageMode("memory");
        assertEquals(StorageMode.MEMORY, m1, "resolveStorageMode('memory') should be MEMORY");

        StorageMode m2 = StorageConfig.resolveStorageMode("oracle");
        assertEquals(StorageMode.ORACLE, m2, "resolveStorageMode('oracle') should be ORACLE");

        StorageMode m3 = StorageConfig.resolveStorageMode("MEMORY");
        assertEquals(StorageMode.MEMORY, m3, "resolveStorageMode('MEMORY') case-insensitive check");
        System.out.println("  [PASS] testValidStorageModes");
    }

    private static void testInvalidAndMissingStorageModes() {
        // Missing mode
        try {
            StorageConfig.resolveStorageMode(null);
            fail("Expected IllegalStateException for null storage mode");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("LOAN_STORAGE_MODE is missing"), "Error mentions missing mode");
        }

        try {
            StorageConfig.resolveStorageMode("   ");
            fail("Expected IllegalStateException for blank storage mode");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("LOAN_STORAGE_MODE is missing"), "Error mentions missing mode");
        }

        // Invalid mode
        try {
            StorageConfig.resolveStorageMode("postgres");
            fail("Expected IllegalStateException for unsupported storage mode 'postgres'");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Unsupported LOAN_STORAGE_MODE"), "Error mentions unsupported mode");
        }
        System.out.println("  [PASS] testInvalidAndMissingStorageModes");
    }

    private static void testValidMlModes() {
        MlMode m1 = MlConfig.resolveMlMode("mock");
        assertEquals(MlMode.MOCK, m1, "resolveMlMode('mock') should be MOCK");

        MlMode m2 = MlConfig.resolveMlMode("http");
        assertEquals(MlMode.HTTP, m2, "resolveMlMode('http') should be HTTP");

        MlMode m3 = MlConfig.resolveMlMode("MOCK");
        assertEquals(MlMode.MOCK, m3, "resolveMlMode('MOCK') case-insensitive check");
        System.out.println("  [PASS] testValidMlModes");
    }

    private static void testInvalidAndMissingMlModes() {
        // Missing mode
        try {
            MlConfig.resolveMlMode(null);
            fail("Expected IllegalStateException for null ML mode");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("LOAN_ML_MODE is missing"), "Error mentions missing ML mode");
        }

        // Invalid mode
        try {
            MlConfig.resolveMlMode("grpc");
            fail("Expected IllegalStateException for unsupported ML mode 'grpc'");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Unsupported LOAN_ML_MODE"), "Error mentions unsupported ML mode");
        }
        System.out.println("  [PASS] testInvalidAndMissingMlModes");
    }

    private static void testMissingHttpBaseUrl() {
        String oldUrl = System.getProperty(MlConfig.PROP_BASE_URL);
        try {
            System.clearProperty(MlConfig.PROP_BASE_URL);
            try {
                MlConfig.getBaseUrl();
                fail("Expected IllegalStateException when LOAN_ML_BASE_URL is not set");
            } catch (IllegalStateException e) {
                assertTrue(e.getMessage().contains("LOAN_ML_BASE_URL is required"), "Error mentions required base URL");
            }

            // Malformed URL (not http/https)
            System.setProperty(MlConfig.PROP_BASE_URL, "ftp://localhost:8000");
            try {
                MlConfig.getBaseUrl();
                fail("Expected IllegalStateException for non-HTTP base URL");
            } catch (IllegalStateException e) {
                assertTrue(e.getMessage().contains("Must specify http or https"), "Error mentions protocol");
            }
        } finally {
            if (oldUrl != null) System.setProperty(MlConfig.PROP_BASE_URL, oldUrl);
            else System.clearProperty(MlConfig.PROP_BASE_URL);
        }
        System.out.println("  [PASS] testMissingHttpBaseUrl");
    }

    private static void testMalformedTimeoutValues() {
        String oldConn = System.getProperty(MlConfig.PROP_CONNECT_TIMEOUT);
        try {
            System.setProperty(MlConfig.PROP_CONNECT_TIMEOUT, "invalid_num");
            try {
                MlConfig.getConnectTimeoutSeconds();
                fail("Expected IllegalStateException for invalid connect timeout");
            } catch (IllegalStateException e) {
                assertTrue(e.getMessage().contains("Invalid integer"), "Error mentions invalid integer");
            }

            System.setProperty(MlConfig.PROP_CONNECT_TIMEOUT, "-5");
            try {
                MlConfig.getConnectTimeoutSeconds();
                fail("Expected IllegalStateException for negative connect timeout");
            } catch (IllegalStateException e) {
                assertTrue(e.getMessage().contains("positive integer"), "Error mentions positive integer");
            }
        } finally {
            if (oldConn != null) System.setProperty(MlConfig.PROP_CONNECT_TIMEOUT, oldConn);
            else System.clearProperty(MlConfig.PROP_CONNECT_TIMEOUT);
        }
        System.out.println("  [PASS] testMalformedTimeoutValues");
    }

    private static void testNoSilentFallbackOnConfigErrors() {
        // Confirm that asking for oracle mode never returns memory mode
        System.setProperty(StorageConfig.PROP_STORAGE_MODE, "oracle");
        assertEquals(StorageMode.ORACLE, StorageConfig.getStorageMode(), "Storage mode strictly oracle");

        System.setProperty(MlConfig.PROP_ML_MODE, "http");
        assertEquals(MlMode.HTTP, MlConfig.getMlMode(), "ML mode strictly http");

        System.clearProperty(StorageConfig.PROP_STORAGE_MODE);
        System.clearProperty(MlConfig.PROP_ML_MODE);
        System.out.println("  [PASS] testNoSilentFallbackOnConfigErrors");
    }

    private static void assertEquals(Object expected, Object actual, String msg) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError("Assertion Failed: " + msg + " [Expected: " + expected + ", Actual: " + actual + "]");
    }

    private static void assertTrue(boolean condition, String msg) {
        if (!condition) {
            throw new AssertionError("Assertion Failed: " + msg);
        }
    }

    private static void fail(String msg) {
        throw new AssertionError("Test Failed: " + msg);
    }
}
