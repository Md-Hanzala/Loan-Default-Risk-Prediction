package com.loanrisk.repository;

import com.loanrisk.model.PredictionRecord;

import java.util.List;

/**
 * Storage-agnostic repository interface for PREDICTION_RECORD entities.
 */
public interface PredictionRecordRepository {

    /**
     * Persists a new PredictionRecord entity linked to an application and assigns its generated identifier.
     *
     * @param record the prediction record to persist
     * @return the generated predictionId
     * @throws StorageException if a storage error occurs
     */
    Long save(PredictionRecord record) throws StorageException;

    /**
     * Retrieves all prediction history records for a given application ID.
     *
     * @param applicationId application ID
     * @return list of prediction records
     * @throws StorageException if a retrieval error occurs
     */
    List<PredictionRecord> findByApplicationId(Long applicationId) throws StorageException;

    /**
     * Clears all stored records (used in test/in-memory environments).
     */
    void clear() throws StorageException;
}
