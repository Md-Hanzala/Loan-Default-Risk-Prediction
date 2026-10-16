package com.loanrisk.ml;

import com.loanrisk.config.MlConfig;
import com.loanrisk.config.MlConfig.MlMode;

/**
 * Factory providing the configured PredictionProvider according to LOAN_ML_MODE.
 * Enforces strict mode resolution without silent fallback.
 */
public class PredictionProviderFactory {

    public static PredictionProvider getPredictionProvider() {
        MlMode mode = MlConfig.getMlMode();
        if (mode == MlMode.MOCK) {
            return new MockPredictionProvider();
        } else if (mode == MlMode.HTTP) {
            return new HttpPredictionProvider(
                    MlConfig.getBaseUrl(),
                    MlConfig.getConnectTimeoutSeconds(),
                    MlConfig.getRequestTimeoutSeconds()
            );
        } else {
            throw new IllegalStateException("Unhandled ML mode: " + mode);
        }
    }
}
