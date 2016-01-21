package com.privatecar.privatecar.utils;

/**
 *  Created by basim on 1/3/16.
 *  TODO: add documentation
 */
public interface RequestListener<T> {
    void onSuccess(T response, String apiName);

    void onFail(String message);
}
