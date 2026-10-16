package com.loanrisk.ml;

/**
 * Exception thrown when an error occurs during ML inference or mock provider execution.
 */
public class PredictionException extends Exception {

    private static final long serialVersionUID = 1L;

    public PredictionException(String message) {
        super(message);
    }

    public PredictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
