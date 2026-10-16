package com.loanrisk.repository.oracle;

import com.loanrisk.config.DatabaseConfig;
import com.loanrisk.model.PredictionRecord;
import com.loanrisk.repository.PredictionRecordRepository;
import com.loanrisk.repository.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Oracle JDBC implementation of PredictionRecordRepository.
 * Uses sequence PREDICTION_RECORD_SEQ and prepared statements against PREDICTION_RECORD table.
 *
 * NOTE: Never silently falls back to in-memory storage on database error.
 */
public class OraclePredictionRecordRepository implements PredictionRecordRepository {

    private static final String SQL_GET_NEXTVAL =
            "SELECT PREDICTION_RECORD_SEQ.NEXTVAL FROM DUAL";

    private static final String SQL_INSERT =
            "INSERT INTO PREDICTION_RECORD (" +
            "  PREDICTION_ID, APPLICATION_ID, MODEL_VERSION, PREDICTED_CLASS, " +
            "  PROBABILITY_B, THRESHOLD_USED, PREDICTION_TIMESTAMP" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_APPLICATION_ID =
            "SELECT PREDICTION_ID, APPLICATION_ID, MODEL_VERSION, PREDICTED_CLASS, " +
            "       PROBABILITY_B, THRESHOLD_USED, PREDICTION_TIMESTAMP " +
            "FROM PREDICTION_RECORD WHERE APPLICATION_ID = ? ORDER BY PREDICTION_TIMESTAMP DESC";

    @Override
    public Long save(PredictionRecord record) throws StorageException {
        if (record == null) {
            throw new IllegalArgumentException("PredictionRecord cannot be null");
        }
        if (record.getApplicationId() == null) {
            throw new IllegalArgumentException("PredictionRecord must reference an applicationId");
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                long id;
                try (PreparedStatement psSeq = conn.prepareStatement(SQL_GET_NEXTVAL);
                     ResultSet rsSeq = psSeq.executeQuery()) {
                    if (rsSeq.next()) {
                        id = rsSeq.getLong(1);
                    } else {
                        throw new SQLException("Failed to obtain next sequence value from PREDICTION_RECORD_SEQ");
                    }
                }

                if (record.getPredictionTimestamp() == null) {
                    record.setPredictionTimestamp(new Timestamp(System.currentTimeMillis()));
                }

                try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
                    ps.setLong(1, id);
                    ps.setLong(2, record.getApplicationId());
                    ps.setString(3, record.getModelVersion());
                    ps.setString(4, record.getPredictedClass());
                    ps.setBigDecimal(5, record.getProbabilityB());
                    ps.setBigDecimal(6, record.getThresholdUsed());
                    ps.setTimestamp(7, record.getPredictionTimestamp());

                    int rows = ps.executeUpdate();
                    if (rows != 1) {
                        throw new SQLException("Failed to insert PREDICTION_RECORD, rows affected: " + rows);
                    }
                }

                conn.commit();
                record.setPredictionId(id);
                return id;
            } catch (SQLException e) {
                conn.rollback();
                throw new StorageException("Failed to persist PREDICTION_RECORD in Oracle (transaction rolled back): "
                        + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new StorageException("Oracle database connection failure while saving PREDICTION_RECORD: "
                    + e.getMessage(), e);
        }
    }

    @Override
    public List<PredictionRecord> findByApplicationId(Long applicationId) throws StorageException {
        if (applicationId == null) {
            return Collections.emptyList();
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_APPLICATION_ID)) {
            ps.setLong(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                List<PredictionRecord> list = new ArrayList<>();
                while (rs.next()) {
                    PredictionRecord record = new PredictionRecord();
                    record.setPredictionId(rs.getLong("PREDICTION_ID"));
                    record.setApplicationId(rs.getLong("APPLICATION_ID"));
                    record.setModelVersion(rs.getString("MODEL_VERSION"));
                    record.setPredictedClass(rs.getString("PREDICTED_CLASS"));
                    record.setProbabilityB(rs.getBigDecimal("PROBABILITY_B"));
                    record.setThresholdUsed(rs.getBigDecimal("THRESHOLD_USED"));
                    record.setPredictionTimestamp(rs.getTimestamp("PREDICTION_TIMESTAMP"));
                    record.setSimulated(false);
                    list.add(record);
                }
                return Collections.unmodifiableList(list);
            }
        } catch (SQLException e) {
            throw new StorageException("Oracle database error while querying PREDICTION_RECORD for application "
                    + applicationId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void clear() throws StorageException {
        throw new UnsupportedOperationException("Clearing data via repository interface is prohibited in Oracle mode.");
    }
}
