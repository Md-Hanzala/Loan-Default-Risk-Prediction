package com.loanrisk.repository;

/**
 * Exception thrown when a persistence error occurs in any repository implementation
 * (Oracle JDBC, In-Memory, etc.).
 */
public class StorageException extends Exception {

    private static final long serialVersionUID = 1L;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
