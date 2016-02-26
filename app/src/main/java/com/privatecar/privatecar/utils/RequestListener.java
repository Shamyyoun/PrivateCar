package com.privatecar.privatecar.utils;

/**
 *  Created by basim on 1/3/16.
 * An interface used as a callback, having functions to be executed when request fail or success.
 */
public interface RequestListener<T> {
    void onSuccess(T response, String apiName);
    void onFail(String message, String apiName);
}
