package com.privatecar.privatecar.utils;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by basim on 1/3/16.
 * //TODO: add documentation
 */

public class RequestHelper<T> {

    private static int TIMEOUT = 60 * 1000; // Ion's default timeout is 30 seconds.
    private static final String LOG_TAG = "request_helper";

    Activity activity;
    String baseUrl;
    String apiName;
    Class<?> cls;
    RequestListener<T> listener;
    Map<String, List<String>> params;
    Map<String, File> files;

    Future<String> future;

    long startTime, finishTime;


    public RequestHelper(Activity activity, String baseUrl, String apiName, @Nullable Class<?> cls, RequestListener<T> listener) {
        this.activity = activity;
        this.baseUrl = baseUrl;
        this.apiName = apiName;
        this.cls = cls;
        this.listener = listener;
    }

    public RequestHelper(Activity activity, String baseUrl, String apiName, @Nullable Class<?> cls, RequestListener<T> listener, Map<String, String> params) {
        this(activity, baseUrl, apiName, cls, listener);
        if (params != null) {
            this.params = new HashMap<>();
            for (String key : params.keySet()) {
                this.params.put(key, Collections.singletonList(params.get(key)));
            }
        }

    }

    public RequestHelper(Activity activity, String baseUrl, String apiName, @Nullable Class<?> cls, RequestListener<T> listener, Map<String, String> params, Map<String, File> files) {
        this(activity, baseUrl, apiName, cls, listener, params);
        this.files = files;
    }


    private void printLogs(int level) {
        switch (level) {
            case 0:
                startTime = System.currentTimeMillis();
                Log.e(LOG_TAG, "'" + apiName + "' request started. time=" + Calendar.getInstance().getTime());
                break;
            case 1:
                finishTime = System.currentTimeMillis();
                Log.e(LOG_TAG, "'" + apiName + "' request finished and parsing started. time=" + Calendar.getInstance().getTime() + ", Time diff: " + (finishTime - startTime) + " MS");
                break;

        }
    }

    public Future<String> executeGet() {
        if (baseUrl == null || apiName == null) {
            throw new IllegalArgumentException("No baseUrl or apiName found.");
        } else {
            printLogs(0);  // request started

            future = Ion.with(activity)
                    .load(baseUrl + apiName)
                    .setTimeout(TIMEOUT)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    public Future<String> executeFormUrlEncoded() {
        if (baseUrl == null || apiName == null) {
            throw new IllegalArgumentException("No baseUrl or apiName found.");
        } else {
            printLogs(0);

            Builders.Any.B ionBuilder = Ion.with(activity)
                    .load("POST", baseUrl + apiName)
                    .setTimeout(TIMEOUT);

            if (params != null) {
                for (String key : params.keySet()) {
                    Log.e(LOG_TAG, "Request Parameter: " + key + "=" + params.get(key));
                }
                ionBuilder.setBodyParameters(params);
            }

            future = ionBuilder.asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    public Future<String> executeMultiPart() {
        if (baseUrl == null || apiName == null || (params == null && files == null)) {
            throw new IllegalArgumentException("No baseUrl, apiName, params or files found.");
        } else {
            printLogs(0);  // request started

            Builders.Any.B ionBuilder =
                    Ion.with(activity)
                            .load("POST", baseUrl + apiName)
                            .setTimeout(TIMEOUT);
//                            .uploadProgress(new ProgressCallback() {
//                                @Override
//                                public void onProgress(long downloaded, long total) {
//                                    Log.e("onProgress()", (int) downloaded + "/ " + total);
//                                }
//                            });

            if (params != null) {
                for (String key : params.keySet()) {
                    Log.e(LOG_TAG, "Request Parameter: " + key + "=" + params.get(key).get(0));
                }

                ionBuilder.setMultipartParameters(params);
            }

            for (String key : files.keySet()) {
                Log.e(LOG_TAG, "Multipart Parameter: " + key + "=" + files.get(key).getAbsolutePath());
                ionBuilder.setMultipartFile(key, "image/jpeg", files.get(key));
            }

            future = ionBuilder.asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    @SuppressWarnings("unchecked")
    private void handleOnCompleted(Exception e, String result) {
        printLogs(1);  // request finished

        if (listener != null) {
            if (e != null) { //on request failure and cancellation
                listener.onFail(e.toString());
            } else if (result != null) {
                Log.e(LOG_TAG, "Response: " + result);

                if (cls == null) { //T must be of type: Object or String
                    listener.onSuccess((T) result, apiName);
                } else {
                    try {
                        listener.onSuccess((T) new Gson().fromJson(result, cls), apiName);
                    } catch (Exception ex) {
                        Log.e(LOG_TAG, "Parsing Exception: " + ex.toString());
                    }
                }
            }
        }
    }

    public boolean cancel(boolean interruptThread) {
        if (!(future.isCancelled() || future.isDone())) {
            return future.cancel(interruptThread);
        }

        return false;
    }


}
