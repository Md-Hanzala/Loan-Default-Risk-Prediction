package com.loanrisk.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Domain model representing an ML prediction record linked to an application,
 * mapping to the PREDICTION_RECORD table.
 */
public class PredictionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long predictionId;
    private Long applicationId;
    private String modelVersion;
    private String predictedClass;
    private BigDecimal probabilityB;
    private BigDecimal thresholdUsed;
    private Timestamp predictionTimestamp;
    private boolean simulated;

    public PredictionRecord() {
    }

    public PredictionRecord(Long predictionId, Long applicationId, String modelVersion,
                            String predictedClass, BigDecimal probabilityB,
                            BigDecimal thresholdUsed, Timestamp predictionTimestamp,
                            boolean simulated) {
        this.predictionId = predictionId;
        this.applicationId = applicationId;
        this.modelVersion = modelVersion;
        this.predictedClass = predictedClass;
        this.probabilityB = probabilityB;
        this.thresholdUsed = thresholdUsed;
        this.predictionTimestamp = predictionTimestamp;
        this.simulated = simulated;
    }

    public Long getPredictionId() {
        return predictionId;
    }

    public void setPredictionId(Long predictionId) {
        this.predictionId = predictionId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getPredictedClass() {
        return predictedClass;
    }

    public void setPredictedClass(String predictedClass) {
        this.predictedClass = predictedClass;
    }

    public BigDecimal getProbabilityB() {
        return probabilityB;
    }

    public void setProbabilityB(BigDecimal probabilityB) {
        this.probabilityB = probabilityB;
    }

    public BigDecimal getThresholdUsed() {
        return thresholdUsed;
    }

    public void setThresholdUsed(BigDecimal thresholdUsed) {
        this.thresholdUsed = thresholdUsed;
    }

    public Timestamp getPredictionTimestamp() {
        return predictionTimestamp;
    }

    public void setPredictionTimestamp(Timestamp predictionTimestamp) {
        this.predictionTimestamp = predictionTimestamp;
    }

    public boolean isSimulated() {
        return simulated;
    }

    public void setSimulated(boolean simulated) {
        this.simulated = simulated;
    }

    @Override
    public String toString() {
        return "PredictionRecord{" +
                "predictionId=" + predictionId +
                ", applicationId=" + applicationId +
                ", modelVersion='" + modelVersion + '\'' +
                ", predictedClass='" + predictedClass + '\'' +
                ", probabilityB=" + probabilityB +
                ", thresholdUsed=" + thresholdUsed +
                ", predictionTimestamp=" + predictionTimestamp +
                ", simulated=" + simulated +
                '}';
    }
}
