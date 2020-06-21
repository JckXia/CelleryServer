package com.cellery.api.backend.shared.Util;

import java.util.function.Function;

/* This is for the purpose of throwing exceptions to the previous stack when using streams */
@FunctionalInterface
public interface CheckedFn<T, R> {
    R apply(T t) throws Exception;

    public static <T,R> Function<T,R> wrap(CheckedFn<T,R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
