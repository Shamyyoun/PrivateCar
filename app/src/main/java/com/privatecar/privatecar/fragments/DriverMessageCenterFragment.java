package com.privatecar.privatecar.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.MessagesRVAdapter;
import com.privatecar.privatecar.models.entities.Message;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.MessagesResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DriverMessageCenterFragment extends BaseFragment implements RequestListener {


    private CheckBox cbMessages;
    private List<Message> messages = new ArrayList<>();
    private RecyclerView rvMessages;
    private MessagesRVAdapter adapter;
    private ProgressDialog progressDialog;

    public DriverMessageCenterFragment() {
        // Required empty public constructor
    }

    private void getMessages(int start) {
        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(getActivity(), R.string.loading_latest_messages);

        User user = AppUtils.getCachedUser(getActivity());
        DriverRequests.getInbox(getActivity(), this, user.getAccessToken(), start);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_message_center, container, false);

        List<Message> cachedMessages = AppUtils.getCachedMessages(getContext());
        if (cachedMessages != null && cachedMessages.size() > 0) {
            messages.addAll(cachedMessages);
            getMessages(cachedMessages.get(cachedMessages.size() - 1).getId());
        } else {
            getMessages(0);
        }

        cbMessages = (CheckBox) fragment.findViewById(R.id.cb_messages);
        cbMessages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Message message : messages) {
                    message.setSelected(isChecked);
                }

                adapter.notifyDataSetChanged();
            }
        });

        rvMessages = (RecyclerView) fragment.findViewById(R.id.rv_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMessages.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvMessages.addItemDecoration(itemDecoration);
        rvMessages.setHasFixedSize(true);
        adapter = new MessagesRVAdapter(messages);
        rvMessages.setAdapter(adapter);

        return fragment;
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        progressDialog.dismiss();

        if (response instanceof MessagesResponse) {
            MessagesResponse messagesResponse = (MessagesResponse) response;

            if (messagesResponse.isSuccess() && messagesResponse.getMessages() != null) {
                messages.addAll(messagesResponse.getMessages());
                adapter.notifyDataSetChanged();

                AppUtils.cacheMessages(getContext(), messages);
            }
        }

    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();

        Utils.showLongToast(getContext(), message);
        Utils.LogE(message);
    }

}
