package com.loanrisk.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages database connectivity settings and JDBC connection creation.
 * Reads connection parameters from environment variables without hardcoded secrets:
 * - LOAN_DB_URL: Oracle JDBC connection URL (e.g. jdbc:oracle:thin:@localhost:1521:XE)
 * - LOAN_DB_USER: Database username
 * - LOAN_DB_PASSWORD: Database password
 */
public class DatabaseConfig {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());

    public static final String ENV_DB_URL = "LOAN_DB_URL";
    public static final String ENV_DB_USER = "LOAN_DB_USER";
    public static final String ENV_DB_PASSWORD = "LOAN_DB_PASSWORD";

    private static final String DEFAULT_URL = "jdbc:oracle:thin:@localhost:1521:XE";

    private static boolean driverRegistered = false;

    static {
        registerDriver();
    }

    private static synchronized void registerDriver() {
        if (!driverRegistered) {
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                driverRegistered = true;
                LOGGER.info("Oracle JDBC Driver successfully loaded.");
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.WARNING,
                        "Oracle JDBC Driver (oracle.jdbc.OracleDriver) not found on classpath. "
                        + "Ensure ojdbc11.jar is provided in Tomcat lib or web/WEB-INF/lib when connecting to Oracle.", e);
            }
        }
    }

    public static String getDbUrl() {
        String url = System.getenv(ENV_DB_URL);
        return (url != null && !url.trim().isEmpty()) ? url.trim() : DEFAULT_URL;
    }

    public static String getDbUser() {
        String user = System.getenv(ENV_DB_USER);
        return (user != null) ? user.trim() : "";
    }

    public static String getDbPassword() {
        String pwd = System.getenv(ENV_DB_PASSWORD);
        return (pwd != null) ? pwd : "";
    }

    public static boolean isConfigured() {
        String user = getDbUser();
        return !user.isEmpty();
    }

    /**
     * Establishes and returns a new JDBC connection using configured environment parameters.
     *
     * @return a live Connection object
     * @throws SQLException if a database access error occurs or credentials are not configured
     */
    public static Connection getConnection() throws SQLException {
        registerDriver();
        String url = getDbUrl();
        String user = getDbUser();
        String pwd = getDbPassword();

        if (user.isEmpty()) {
            throw new SQLException("Database credentials not configured. Please set environment variable "
                    + ENV_DB_USER + " (and optionally " + ENV_DB_PASSWORD + ").");
        }

        return DriverManager.getConnection(url, user, pwd);
    }
}
