package com.privateegy.privatecar.activities;

import android.os.Bundle;
import android.view.View;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.custom_views.EmptyView;
import com.privateegy.privatecar.custom_views.ErrorView;
import com.privateegy.privatecar.custom_views.ProgressView;

/**
 * Created by Shamyyoun on 3/9/2016.
 */
public abstract class ProgressActivity extends BaseActivity {
    private ProgressView progressView;
    private ErrorView errorView;
    private EmptyView emptyView;
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        // init views
        progressView = (ProgressView) findViewById(R.id.progress_view);
        errorView = (ErrorView) findViewById(R.id.error_view);
        emptyView = (EmptyView) findViewById(R.id.empty_view);
        mainView = findViewById(getMainViewResId());

        // add refresh click listener if error view is not null
        if (errorView != null) {
            errorView.setRefreshListener(getRefreshListener());
        }
    }

    protected abstract int getContentViewResId();

    protected abstract int getMainViewResId();

    protected abstract View.OnClickListener getRefreshListener();

    protected void showProgress() {
        if (progressView != null)
            progressView.setVisibility(View.VISIBLE);
        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (mainView != null)
            mainView.setVisibility(View.GONE);
    }

    protected void showError(String msg) {
        if (progressView != null)
            progressView.setVisibility(View.GONE);
        if (errorView != null) {
            errorView.setError(msg);
            errorView.setVisibility(View.VISIBLE);
        }
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (mainView != null)
            mainView.setVisibility(View.GONE);
    }

    protected void showError(int msgResId) {
        showError(getString(msgResId));
    }

    protected void showEmpty(String msg) {
        if (progressView != null)
            progressView.setVisibility(View.GONE);
        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (emptyView != null) {
            emptyView.setEmpty(msg);
            emptyView.setVisibility(View.VISIBLE);
        }
        if (mainView != null)
            mainView.setVisibility(View.GONE);
    }

    protected void showEmpty(int msgResId) {
        showEmpty(getString(msgResId));
    }

    protected void showMain() {
        if (progressView != null)
            progressView.setVisibility(View.GONE);
        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (mainView != null)
            mainView.setVisibility(View.VISIBLE);
    }
}
