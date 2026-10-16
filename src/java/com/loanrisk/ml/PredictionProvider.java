package com.loanrisk.ml;

import com.loanrisk.model.LoanApplication;
import com.loanrisk.model.PredictionRecord;

/**
 * Stable service interface for executing loan risk prediction.
 * Designed to be implemented either by a local mock provider during development
 * or by an HTTP client communicating with the external Python FastAPI service in production.
 */
public interface PredictionProvider {

    /**
     * Obtains a prediction for the given loan application.
     *
     * @param application the loan application containing the nine input features
     * @return a PredictionRecord containing model version, predicted class ('G' or 'B'),
     *         probability for class B, and threshold used
     * @throws PredictionException if inference fails or inputs cannot be processed
     */
    PredictionRecord predict(LoanApplication application) throws PredictionException;

    /**
     * Returns true if this provider produces simulated/mock results rather than real ML inference.
     */
    boolean isSimulated();
}
