package com.loanrisk.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Encapsulates validation findings for loan application requests.
 */
public class ValidationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> errors = new LinkedHashMap<>();

    public void addError(String field, String message) {
        errors.put(field, message);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

    @Override
    public String toString() {
        return "ValidationResult{errors=" + errors + '}';
    }
}
