package com.rjx.network.errorhandler;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Jianxiong Rao
 * @date 2020-04-15
 */
public class HttpErrorHandle<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandle.handleException(throwable));
    }
}
