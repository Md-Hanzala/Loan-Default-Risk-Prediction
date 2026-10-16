package com.loanrisk.ml;

import com.loanrisk.model.LoanApplication;
import com.loanrisk.model.PredictionRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * SIMULATED / MOCK implementation of PredictionProvider.
 *
 * IMPORTANT NOTICE:
 * This provider generates purely synthetic, simulated results for development, testing,
 * and vertical workflow verification. It DOES NOT execute a trained machine learning model,
 * and its outputs MUST NEVER be interpreted as genuine credit risk assessments or actual
 * loan default probabilities.
 */
public class MockPredictionProvider implements PredictionProvider {

    public static final String MODEL_VERSION = "mock-v1-simulated";
    public static final BigDecimal DEFAULT_THRESHOLD = new BigDecimal("0.3000");

    @Override
    public PredictionRecord predict(LoanApplication application) throws PredictionException {
        if (application == null) {
            throw new PredictionException("Cannot execute simulated prediction: LoanApplication is null.");
        }

        // Generate a deterministic, simulated probability based on applicant inputs
        // purely to produce realistic, non-random numbers in range [0.0000, 1.0000]
        double score = 0.20; // baseline

        if (application.getDuePayment() != null && application.getDuePayment().compareTo(BigDecimal.ZERO) > 0) {
            score += 0.15;
        }

        if (application.getAccCurrentBalance() != null && application.getAccCurrentBalance().compareTo(BigDecimal.ZERO) < 0) {
            score += 0.20;
        }

        if (application.getInvestmentTotal() != null && application.getInstallSize() != null
                && application.getInstallSize().compareTo(BigDecimal.ZERO) > 0) {
            double ratio = application.getInstallSize().doubleValue() / Math.max(1.0, application.getInvestmentTotal().doubleValue());
            if (ratio > 0.10) {
                score += 0.10;
            }
        }

        // Clamp between 0.0100 and 0.9900
        score = Math.max(0.0100, Math.min(0.9900, score));

        BigDecimal probB = BigDecimal.valueOf(score).setScale(6, RoundingMode.HALF_UP);
        BigDecimal threshold = DEFAULT_THRESHOLD;

        // In the Kaggle experimental contract: if probB >= threshold => 'B', else => 'G'
        String predictedClass = (probB.compareTo(threshold) >= 0) ? "B" : "G";

        PredictionRecord record = new PredictionRecord();
        record.setModelVersion(MODEL_VERSION);
        record.setPredictedClass(predictedClass);
        record.setProbabilityB(probB);
        record.setThresholdUsed(threshold);
        record.setPredictionTimestamp(new Timestamp(System.currentTimeMillis()));
        record.setSimulated(true);

        return record;
    }

    @Override
    public boolean isSimulated() {
        return true;
    }
}
