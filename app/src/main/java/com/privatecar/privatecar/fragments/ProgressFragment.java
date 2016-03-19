package com.privatecar.privatecar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.custom_views.EmptyView;
import com.privatecar.privatecar.custom_views.ErrorView;
import com.privatecar.privatecar.custom_views.ProgressView;

/**
 * Created by Shamyyoun on 3/9/2016.
 */
public abstract class ProgressFragment extends BaseFragment {
    protected View rootView;
    private ProgressView progressView;
    private ErrorView errorView;
    private EmptyView emptyView;
    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentViewResId(), container, false);

        // init views
        progressView = (ProgressView) rootView.findViewById(R.id.progress_view);
        errorView = (ErrorView) rootView.findViewById(R.id.error_view);
        emptyView = (EmptyView) rootView.findViewById(R.id.empty_view);
        mainView = rootView.findViewById(getMainViewResId());

        // add refresh click listener if error view is not null
        if (errorView != null) {
            errorView.setRefreshListener(getRefreshListener());
        }

        return rootView;
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
