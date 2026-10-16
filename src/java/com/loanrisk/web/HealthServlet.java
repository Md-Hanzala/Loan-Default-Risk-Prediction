package com.loanrisk.web;

import com.loanrisk.config.DatabaseConfig;
import com.loanrisk.config.MlConfig;
import com.loanrisk.config.StorageConfig;
import com.loanrisk.config.StorageConfig.StorageMode;
import com.loanrisk.repository.RepositoryFactory;
import com.loanrisk.service.LoanApplicationService;
import com.loanrisk.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Health check endpoint reporting application, storage mode, and dependency status.
 * Complies with requirement: Clearly displays the active LOAN_STORAGE_MODE and never
 * claims Oracle or ML service is available unless actually verified.
 */
@WebServlet(name = "HealthServlet", urlPatterns = {"/api/health"})
public class HealthServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private LoanApplicationService applicationService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.applicationService = new LoanApplicationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", "UP");
        responseMap.put("timestamp", Instant.now().toString());

        // Runtime details
        Map<String, String> runtime = new LinkedHashMap<>();
        runtime.put("javaVersion", System.getProperty("java.version"));
        runtime.put("serverInfo", getServletContext().getServerInfo());
        responseMap.put("runtime", runtime);

        // Storage Mode reporting
        Map<String, Object> storageMap = new LinkedHashMap<>();
        StorageMode mode;
        try {
            mode = StorageConfig.getStorageMode();
            storageMap.put("mode", mode.getCode());
        } catch (Exception e) {
            storageMap.put("mode", "CONFIG_ERROR");
            storageMap.put("error", e.getMessage());
            mode = null;
        }

        if (mode == StorageMode.MEMORY) {
            storageMap.put("status", "IN_MEMORY_ACTIVE");
            storageMap.put("disclaimer", "Running in-memory storage mode. Records will be lost when application restarts and must never be used for production.");
            try {
                int count = RepositoryFactory.getLoanApplicationRepository().findAll().size();
                storageMap.put("storedApplicationsCount", count);
            } catch (Exception e) {
                storageMap.put("storedApplicationsCount", "UNAVAILABLE");
            }
        } else if (mode == StorageMode.ORACLE) {
            storageMap.put("status", "ORACLE_MODE");
            storageMap.put("url", DatabaseConfig.getDbUrl());
            boolean configured = DatabaseConfig.isConfigured();
            storageMap.put("configured", configured);

            if (!configured) {
                storageMap.put("connectivity", "NOT_CONFIGURED");
                storageMap.put("message", "Database user/password not provided via environment variables (LOAN_DB_USER).");
            } else {
                try (Connection conn = DatabaseConfig.getConnection()) {
                    try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM DUAL");
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            storageMap.put("connectivity", "CONNECTED");
                            storageMap.put("verified", true);
                        } else {
                            storageMap.put("connectivity", "UNEXPECTED_RESPONSE");
                            storageMap.put("verified", false);
                        }
                    }
                } catch (SQLException e) {
                    storageMap.put("connectivity", "UNREACHABLE");
                    storageMap.put("verified", false);
                    storageMap.put("error", e.getMessage());
                }
            }
        }
        responseMap.put("storage", storageMap);

        // ML inference provider check
        Map<String, Object> mlStatus = new LinkedHashMap<>();
        try {
            MlConfig.MlMode mlMode = MlConfig.getMlMode();
            mlStatus.put("mode", mlMode.getCode());
        } catch (Exception e) {
            mlStatus.put("mode", "CONFIG_ERROR");
            mlStatus.put("error", e.getMessage());
        }

        boolean isSimulated = applicationService.getPredictionProvider().isSimulated();
        mlStatus.put("simulated", isSimulated);
        mlStatus.put("providerClass", applicationService.getPredictionProvider().getClass().getSimpleName());

        if (isSimulated) {
            mlStatus.put("status", "MOCK_PROVIDER_ACTIVE");
            mlStatus.put("disclaimer", "Using simulated MockPredictionProvider. Outputs are for development and testing only, not genuine ML predictions.");
        } else {
            mlStatus.put("status", "HTTP_SERVICE_CONFIGURED");
            try {
                mlStatus.put("baseUrl", MlConfig.getBaseUrl());
                mlStatus.put("connectivity", "UNVERIFIED (Awaiting external Python service startup)");
            } catch (Exception e) {
                mlStatus.put("configError", e.getMessage());
            }
        }
        responseMap.put("mlService", mlStatus);

        resp.setStatus(HttpServletResponse.SC_OK);
        try (PrintWriter out = resp.getWriter()) {
            out.print(JsonUtil.toJson(responseMap));
            out.flush();
        }
    }
}
