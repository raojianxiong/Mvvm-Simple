package com.rjx.base.model;

public interface MvvmNetworkObserver<F> {
    void onSuccess(F t,boolean isFromCache);
    void onFailure(Throwable e);
}
