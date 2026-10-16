package com.loanrisk.web;

import com.loanrisk.config.MlConfig;
import com.loanrisk.config.StorageConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Logger;

/**
 * ServletContextListener initializing application defaults from web.xml context-params.
 */
@WebListener
public class AppLifecycleListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppLifecycleListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        String storageMode = ctx.getInitParameter("LOAN_STORAGE_MODE");
        if (storageMode != null && !storageMode.trim().isEmpty()) {
            StorageConfig.setDefaultModeFromContext(storageMode.trim());
            LOGGER.info("Configured default LOAN_STORAGE_MODE from web context: " + storageMode);
        }

        String mlMode = ctx.getInitParameter("LOAN_ML_MODE");
        if (mlMode != null && !mlMode.trim().isEmpty()) {
            MlConfig.setDefaultModeFromContext(mlMode.trim());
            LOGGER.info("Configured default LOAN_ML_MODE from web context: " + mlMode);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // cleanup if needed
    }
}
