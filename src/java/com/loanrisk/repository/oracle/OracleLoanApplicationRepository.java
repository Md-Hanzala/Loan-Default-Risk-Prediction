package com.loanrisk.repository.oracle;

import com.loanrisk.config.DatabaseConfig;
import com.loanrisk.model.LoanApplication;
import com.loanrisk.repository.LoanApplicationRepository;
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
 * Oracle JDBC implementation of LoanApplicationRepository.
 * Uses sequence LOAN_APPLICATION_SEQ and prepared statements against LOAN_APPLICATION table.
 *
 * NOTE: Never silently falls back to in-memory storage on database error.
 */
public class OracleLoanApplicationRepository implements LoanApplicationRepository {

    private static final String SQL_GET_NEXTVAL =
            "SELECT LOAN_APPLICATION_SEQ.NEXTVAL FROM DUAL";

    private static final String SQL_INSERT =
            "INSERT INTO LOAN_APPLICATION (" +
            "  APPLICATION_ID, INVESTMENT_TOTAL, ACCCURRENTBALANCE, INF_MARITAL_STATUS, " +
            "  INF_GENDER, INSTALL_SIZE, DUE_PAYMENT, COMPENSATION_CHARGED, " +
            "  CLIENT_TYPE, REPAY_MODE, CREATED_AT, UPDATED_AT" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID =
            "SELECT APPLICATION_ID, INVESTMENT_TOTAL, ACCCURRENTBALANCE, INF_MARITAL_STATUS, " +
            "       INF_GENDER, INSTALL_SIZE, DUE_PAYMENT, COMPENSATION_CHARGED, " +
            "       CLIENT_TYPE, REPAY_MODE, CREATED_AT, UPDATED_AT " +
            "FROM LOAN_APPLICATION WHERE APPLICATION_ID = ?";

    private static final String SQL_FIND_ALL =
            "SELECT APPLICATION_ID, INVESTMENT_TOTAL, ACCCURRENTBALANCE, INF_MARITAL_STATUS, " +
            "       INF_GENDER, INSTALL_SIZE, DUE_PAYMENT, COMPENSATION_CHARGED, " +
            "       CLIENT_TYPE, REPAY_MODE, CREATED_AT, UPDATED_AT " +
            "FROM LOAN_APPLICATION ORDER BY APPLICATION_ID DESC";

    @Override
    public Long save(LoanApplication application) throws StorageException {
        if (application == null) {
            throw new IllegalArgumentException("LoanApplication cannot be null");
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
                        throw new SQLException("Failed to obtain next sequence value from LOAN_APPLICATION_SEQ");
                    }
                }

                Timestamp now = new Timestamp(System.currentTimeMillis());
                if (application.getCreatedAt() == null) {
                    application.setCreatedAt(now);
                }
                if (application.getUpdatedAt() == null) {
                    application.setUpdatedAt(now);
                }

                try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
                    ps.setLong(1, id);
                    ps.setBigDecimal(2, application.getInvestmentTotal());
                    ps.setBigDecimal(3, application.getAccCurrentBalance());
                    ps.setString(4, application.getInfMaritalStatus());
                    ps.setString(5, application.getInfGender());
                    ps.setBigDecimal(6, application.getInstallSize());
                    ps.setBigDecimal(7, application.getDuePayment());
                    ps.setString(8, application.getCompensationCharged());
                    ps.setString(9, application.getClientType());
                    ps.setString(10, application.getRepayMode());
                    ps.setTimestamp(11, application.getCreatedAt());
                    ps.setTimestamp(12, application.getUpdatedAt());

                    int rows = ps.executeUpdate();
                    if (rows != 1) {
                        throw new SQLException("Failed to insert LOAN_APPLICATION, rows affected: " + rows);
                    }
                }

                conn.commit();
                application.setApplicationId(id);
                return id;
            } catch (SQLException e) {
                conn.rollback();
                throw new StorageException("Failed to persist LOAN_APPLICATION in Oracle (transaction rolled back): "
                        + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new StorageException("Oracle database connection failure while saving LOAN_APPLICATION: "
                    + e.getMessage(), e);
        }
    }

    @Override
    public LoanApplication findById(Long id) throws StorageException {
        if (id == null) {
            return null;
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new StorageException("Oracle database error while querying LOAN_APPLICATION by ID " + id + ": "
                    + e.getMessage(), e);
        }
    }

    @Override
    public List<LoanApplication> findAll() throws StorageException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<LoanApplication> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return Collections.unmodifiableList(list);
        } catch (SQLException e) {
            throw new StorageException("Oracle database error while querying LOAN_APPLICATION list: " + e.getMessage(), e);
        }
    }

    @Override
    public void clear() throws StorageException {
        throw new UnsupportedOperationException("Clearing data via repository interface is prohibited in Oracle mode.");
    }

    private LoanApplication mapRow(ResultSet rs) throws SQLException {
        LoanApplication app = new LoanApplication();
        app.setApplicationId(rs.getLong("APPLICATION_ID"));
        app.setInvestmentTotal(rs.getBigDecimal("INVESTMENT_TOTAL"));
        app.setAccCurrentBalance(rs.getBigDecimal("ACCCURRENTBALANCE"));
        app.setInfMaritalStatus(rs.getString("INF_MARITAL_STATUS"));
        app.setInfGender(rs.getString("INF_GENDER"));
        app.setInstallSize(rs.getBigDecimal("INSTALL_SIZE"));
        app.setDuePayment(rs.getBigDecimal("DUE_PAYMENT"));
        app.setCompensationCharged(rs.getString("COMPENSATION_CHARGED"));
        app.setClientType(rs.getString("CLIENT_TYPE"));
        app.setRepayMode(rs.getString("REPAY_MODE"));
        app.setCreatedAt(rs.getTimestamp("CREATED_AT"));
        app.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
        return app;
    }
}
