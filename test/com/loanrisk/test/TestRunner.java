package com.loanrisk.test;

/**
 * Master test runner executing the complete automated test suite
 * (Configuration tests, HTTP ML contract tests, Application workflow tests).
 */
public class TestRunner {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("  LOAN DEFAULT RISK PREDICTION - AUTOMATED TEST SUITE (PHASE 12-14)");
        System.out.println("================================================================================");

        long startTime = System.currentTimeMillis();
        int passedSuites = 0;
        int failedSuites = 0;

        // Suite 1: Configuration Tests
        try {
            ConfigurationTest.runAll();
            passedSuites++;
        } catch (Throwable t) {
            System.err.println("FAILED: ConfigurationTest - " + t.getMessage());
            t.printStackTrace();
            failedSuites++;
        }

        System.out.println();

        // Suite 2: HTTP Prediction Provider Tests
        try {
            HttpPredictionProviderTest.runAll();
            passedSuites++;
        } catch (Throwable t) {
            System.err.println("FAILED: HttpPredictionProviderTest - " + t.getMessage());
            t.printStackTrace();
            failedSuites++;
        }

        System.out.println();

        // Suite 3: Application Workflow Tests
        try {
            ApplicationWorkflowTest.runAll();
            passedSuites++;
        } catch (Throwable t) {
            System.err.println("FAILED: ApplicationWorkflowTest - " + t.getMessage());
            t.printStackTrace();
            failedSuites++;
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("================================================================================");
        System.out.println("TEST EXECUTION SUMMARY");
        System.out.println("================================================================================");
        System.out.println("  Suites Executed : " + (passedSuites + failedSuites));
        System.out.println("  Suites Passed   : " + passedSuites);
        System.out.println("  Suites Failed   : " + failedSuites);
        System.out.println("  Total Duration  : " + duration + " ms");
        System.out.println("================================================================================");

        if (failedSuites > 0) {
            System.exit(1);
        } else {
            System.out.println("ALL AUTOMATED TESTS PASSED SUCCESSFULLY!");
            System.exit(0);
        }
    }
}
