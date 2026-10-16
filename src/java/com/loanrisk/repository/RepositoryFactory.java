package com.loanrisk.repository;

import com.loanrisk.config.StorageConfig;
import com.loanrisk.config.StorageConfig.StorageMode;
import com.loanrisk.repository.memory.InMemoryLoanApplicationRepository;
import com.loanrisk.repository.memory.InMemoryPredictionRecordRepository;
import com.loanrisk.repository.oracle.OracleLoanApplicationRepository;
import com.loanrisk.repository.oracle.OraclePredictionRecordRepository;

import java.util.logging.Logger;

/**
 * Factory providing repository instances according to the configured LOAN_STORAGE_MODE.
 */
public class RepositoryFactory {

    private static final Logger LOGGER = Logger.getLogger(RepositoryFactory.class.getName());

    // In-memory shared singletons
    private static final InMemoryLoanApplicationRepository IN_MEMORY_APP_REPO =
            new InMemoryLoanApplicationRepository();
    private static final InMemoryPredictionRecordRepository IN_MEMORY_PRED_REPO =
            new InMemoryPredictionRecordRepository();

    // Oracle repository singletons
    private static final OracleLoanApplicationRepository ORACLE_APP_REPO =
            new OracleLoanApplicationRepository();
    private static final OraclePredictionRecordRepository ORACLE_PRED_REPO =
            new OraclePredictionRecordRepository();

    public static LoanApplicationRepository getLoanApplicationRepository() {
        StorageMode mode = StorageConfig.getStorageMode();
        if (mode == StorageMode.MEMORY) {
            return IN_MEMORY_APP_REPO;
        } else if (mode == StorageMode.ORACLE) {
            return ORACLE_APP_REPO;
        } else {
            throw new IllegalStateException("Unhandled storage mode: " + mode);
        }
    }

    public static PredictionRecordRepository getPredictionRecordRepository() {
        StorageMode mode = StorageConfig.getStorageMode();
        if (mode == StorageMode.MEMORY) {
            return IN_MEMORY_PRED_REPO;
        } else if (mode == StorageMode.ORACLE) {
            return ORACLE_PRED_REPO;
        } else {
            throw new IllegalStateException("Unhandled storage mode: " + mode);
        }
    }
}
