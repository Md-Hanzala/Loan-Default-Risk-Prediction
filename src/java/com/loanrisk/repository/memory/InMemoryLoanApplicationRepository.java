package com.loanrisk.repository.memory;

import com.loanrisk.model.LoanApplication;
import com.loanrisk.repository.LoanApplicationRepository;
import com.loanrisk.repository.StorageException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe in-memory repository for LOAN_APPLICATION entities.
 * Designed for local development, demonstration, and isolated workflow verification.
 *
 * NOTE: Records stored here reside purely in JVM memory, disappear when the
 * application stops or restarts, and must NEVER be used for production persistence.
 */
public class InMemoryLoanApplicationRepository implements LoanApplicationRepository {

    private final Map<Long, LoanApplication> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1000);

    @Override
    public Long save(LoanApplication application) throws StorageException {
        if (application == null) {
            throw new StorageException("Cannot save null LoanApplication.");
        }

        long id = sequence.incrementAndGet();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        application.setApplicationId(id);
        if (application.getCreatedAt() == null) {
            application.setCreatedAt(now);
        }
        if (application.getUpdatedAt() == null) {
            application.setUpdatedAt(now);
        }

        // Store a defensive clone/reference
        store.put(id, copy(application));
        return id;
    }

    @Override
    public LoanApplication findById(Long id) throws StorageException {
        if (id == null) {
            return null;
        }
        LoanApplication app = store.get(id);
        return (app != null) ? copy(app) : null;
    }

    @Override
    public List<LoanApplication> findAll() throws StorageException {
        List<LoanApplication> list = new ArrayList<>();
        for (LoanApplication app : store.values()) {
            list.add(copy(app));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public void clear() {
        store.clear();
        sequence.set(1000);
    }

    private LoanApplication copy(LoanApplication source) {
        return new LoanApplication(
                source.getApplicationId(),
                source.getInvestmentTotal(),
                source.getAccCurrentBalance(),
                source.getInfMaritalStatus(),
                source.getInfGender(),
                source.getInstallSize(),
                source.getDuePayment(),
                source.getCompensationCharged(),
                source.getClientType(),
                source.getRepayMode(),
                source.getCreatedAt(),
                source.getUpdatedAt()
        );
    }
}
