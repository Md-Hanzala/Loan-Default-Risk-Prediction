package com.loanrisk.repository.memory;

import com.loanrisk.model.PredictionRecord;
import com.loanrisk.repository.PredictionRecordRepository;
import com.loanrisk.repository.StorageException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe in-memory repository for PREDICTION_RECORD entities.
 * Designed for local development, demonstration, and isolated workflow verification.
 *
 * NOTE: Records stored here reside purely in JVM memory, disappear when the
 * application stops or restarts, and must NEVER be used for production persistence.
 */
public class InMemoryPredictionRecordRepository implements PredictionRecordRepository {

    private final Map<Long, PredictionRecord> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(5000);

    @Override
    public Long save(PredictionRecord record) throws StorageException {
        if (record == null) {
            throw new StorageException("Cannot save null PredictionRecord.");
        }
        if (record.getApplicationId() == null) {
            throw new StorageException("PredictionRecord must reference an applicationId.");
        }

        long id = sequence.incrementAndGet();
        record.setPredictionId(id);
        if (record.getPredictionTimestamp() == null) {
            record.setPredictionTimestamp(new Timestamp(System.currentTimeMillis()));
        }

        store.put(id, copy(record));
        return id;
    }

    @Override
    public List<PredictionRecord> findByApplicationId(Long applicationId) throws StorageException {
        List<PredictionRecord> results = new ArrayList<>();
        if (applicationId == null) {
            return results;
        }

        for (PredictionRecord record : store.values()) {
            if (applicationId.equals(record.getApplicationId())) {
                results.add(copy(record));
            }
        }
        // Sort descending by timestamp or ID
        results.sort((a, b) -> Long.compare(b.getPredictionId(), a.getPredictionId()));
        return Collections.unmodifiableList(results);
    }

    @Override
    public void clear() {
        store.clear();
        sequence.set(5000);
    }

    private PredictionRecord copy(PredictionRecord source) {
        return new PredictionRecord(
                source.getPredictionId(),
                source.getApplicationId(),
                source.getModelVersion(),
                source.getPredictedClass(),
                source.getProbabilityB(),
                source.getThresholdUsed(),
                source.getPredictionTimestamp(),
                source.isSimulated()
        );
    }
}
