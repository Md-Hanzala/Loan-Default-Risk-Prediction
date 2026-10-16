package com.loanrisk.service;

import com.loanrisk.model.LoanApplication;
import com.loanrisk.model.PredictionRecord;

import java.io.Serializable;

/**
 * Result object returned by LoanApplicationService encapsulating the lifecycle
 * state, generated IDs, and potential failures at each processing stage.
 */
public class ApplicationProcessingResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Status {
        SUCCESS,
        VALIDATION_FAILED,
        DATABASE_ERROR,
        PREDICTION_FAILED,
        PREDICTION_PERSISTENCE_FAILED
    }

    private final Status status;
    private final LoanApplication application;
    private final PredictionRecord prediction;
    private final ValidationResult validationResult;
    private final String errorMessage;

    public ApplicationProcessingResult(Status status, LoanApplication application,
                                       PredictionRecord prediction, ValidationResult validationResult,
                                       String errorMessage) {
        this.status = status;
        this.application = application;
        this.prediction = prediction;
        this.validationResult = validationResult;
        this.errorMessage = errorMessage;
    }

    public static ApplicationProcessingResult success(LoanApplication app, PredictionRecord pred) {
        return new ApplicationProcessingResult(Status.SUCCESS, app, pred, null, null);
    }

    public static ApplicationProcessingResult validationFailed(ValidationResult vr) {
        return new ApplicationProcessingResult(Status.VALIDATION_FAILED, null, null, vr, "Validation failed");
    }

    public static ApplicationProcessingResult databaseError(String message) {
        return new ApplicationProcessingResult(Status.DATABASE_ERROR, null, null, null, message);
    }

    public static ApplicationProcessingResult predictionFailed(LoanApplication app, String message) {
        return new ApplicationProcessingResult(Status.PREDICTION_FAILED, app, null, null, message);
    }

    public static ApplicationProcessingResult predictionPersistenceFailed(LoanApplication app,
                                                                         PredictionRecord pred,
                                                                         String message) {
        return new ApplicationProcessingResult(Status.PREDICTION_PERSISTENCE_FAILED, app, pred, null, message);
    }

    public Status getStatus() {
        return status;
    }

    public LoanApplication getApplication() {
        return application;
    }

    public PredictionRecord getPrediction() {
        return prediction;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getApplicationId() {
        return (application != null) ? application.getApplicationId() : null;
    }

    public Long getPredictionId() {
        return (prediction != null) ? prediction.getPredictionId() : null;
    }
}
