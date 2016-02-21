package com.electric3.server.utils;

import com.electric3.dataatoms.IProgressProvider;

import java.util.List;

public enum ProgressCalculator {
    ME;
    public int calculateProgress(List<? extends IProgressProvider> progressProviders) {
        int result = 0;

        for(IProgressProvider iProgressProvider : progressProviders) {
            iProgressProvider.getProgress();
        }

        return result;
    }
}
