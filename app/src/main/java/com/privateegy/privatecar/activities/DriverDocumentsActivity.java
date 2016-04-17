package com.privateegy.privatecar.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.adapters.DriverDocumentsRVAdapter;
import com.privateegy.privatecar.models.entities.Documents;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.responses.DocumentsResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

public class DriverDocumentsActivity extends ProgressBackActivity implements RequestListener<DocumentsResponse> {
    private RecyclerView rvDocuments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // customize recycler view
        rvDocuments = (RecyclerView) findViewById(R.id.rv_documents);
//        rvDocuments.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        rvDocuments.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvDocuments.setHasFixedSize(true);

        loadDocuments();
    }

    private void loadDocuments() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            showError(R.string.no_internet_connection);
            return;
        }

        // show progress
        showProgress();

        // create & send the request
        User user = AppUtils.getCachedUser(this);
        DriverRequests.documents(this, this, user.getAccessToken());
    }

    @Override
    public void onSuccess(DocumentsResponse response, String apiName) {
        // check the response
        if (response.isStatus()) {
            // check documents
            if (response.getDocuments() == null) {
                // show empty
                showEmpty(R.string.no_documents_stored_in_your_account);
            } else if (Utils.isNullOrEmpty(response.getDocuments().getAsArray())) {
                // show empty
                showEmpty(R.string.no_documents_stored_in_your_account);
            } else {
                // update the ui
                updateUI(response.getDocuments());
            }
        } else {
            showError(R.string.unexpected_error_try_again);
        }
    }

    private void updateUI(Documents documents) {
        DriverDocumentsRVAdapter adapter = new DriverDocumentsRVAdapter(this, documents.getAsArray());
        rvDocuments.setAdapter(adapter);
        showMain();
    }

    @Override
    public void onFail(String message, String apiName) {
        showError(R.string.connection_error);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_driver_documents;
    }

    @Override
    protected int getMainViewResId() {
        return R.id.rv_documents;
    }

    @Override
    protected View.OnClickListener getRefreshListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDocuments();
            }
        };
    }
}
