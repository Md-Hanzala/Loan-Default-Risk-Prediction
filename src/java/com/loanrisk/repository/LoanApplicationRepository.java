package com.loanrisk.repository;

import com.loanrisk.model.LoanApplication;

import java.util.List;

/**
 * Storage-agnostic repository interface for LOAN_APPLICATION entities.
 */
public interface LoanApplicationRepository {

    /**
     * Persists a new LoanApplication entity and assigns its generated identifier.
     *
     * @param application the entity to persist
     * @return the generated applicationId
     * @throws StorageException if a storage error occurs
     */
    Long save(LoanApplication application) throws StorageException;

    /**
     * Retrieves a LoanApplication by its primary key.
     *
     * @param id primary key
     * @return entity or null if not found
     * @throws StorageException if a retrieval error occurs
     */
    LoanApplication findById(Long id) throws StorageException;

    /**
     * Retrieves all saved applications.
     */
    List<LoanApplication> findAll() throws StorageException;

    /**
     * Clears all stored records (used in test/in-memory environments).
     */
    void clear() throws StorageException;
}
