package com.electric3.server.utils;

import com.electric3.dataatoms.IStatusProvider;
import com.electric3.dataatoms.StatusEnum;

import java.util.List;

public enum  StatusCalculator {
    ME;

    public StatusEnum selectWorse( StatusEnum left, StatusEnum right ) {
        return left.ordinal() < right.ordinal() ? left : right;
    }

    public StatusEnum selectWorseOnSet(List<? extends IStatusProvider> statusProviders) {
        StatusEnum result = StatusEnum.GREEN;

        for( IStatusProvider statusProvider : statusProviders ) {
            result = selectWorse(statusProvider.getStatus(), result);
        }

        return result;
    }
}
