package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverCreateMessageActivity;
import com.privatecar.privatecar.activities.DriverMessageDetailsActivity;
import com.privatecar.privatecar.adapters.MessagesRVAdapter;
import com.privatecar.privatecar.interfaces.ItemClickListener;
import com.privatecar.privatecar.models.entities.Message;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.MessagesResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DriverMessageCenterFragment extends BaseFragment implements RequestListener, ItemClickListener {


    private CheckBox cbMessages;
    private ImageButton ibDeleteMessages, ibNewMessage;
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

        ibDeleteMessages = (ImageButton) fragment.findViewById(R.id.ib_delete_messages);
        ibDeleteMessages.setOnClickListener(this);
        ibNewMessage = (ImageButton) fragment.findViewById(R.id.ib_new_message);
        ibNewMessage.setOnClickListener(this);

        rvMessages = (RecyclerView) fragment.findViewById(R.id.rv_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMessages.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvMessages.addItemDecoration(itemDecoration);
        rvMessages.setHasFixedSize(true);
        adapter = new MessagesRVAdapter(messages, this);
        rvMessages.setAdapter(adapter);

        return fragment;
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        progressDialog.dismiss();

        if (response instanceof MessagesResponse) {
            MessagesResponse messagesResponse = (MessagesResponse) response;

            if (messagesResponse.isSuccess() && messagesResponse.getMessages() != null) {
                messages.addAll(0, messagesResponse.getMessages());
                adapter.notifyDataSetChanged();
                AppUtils.cacheMessages(getContext(), messages);
            }
        } else if (response instanceof GeneralResponse) {
            GeneralResponse generalResponse = (GeneralResponse) response;
            if (generalResponse.isSuccess()) {
                Iterator<Message> messageIterator = messages.iterator();
                while (messageIterator.hasNext()) {
                    Message message = messageIterator.next();
                    if (message.isSelected()) messageIterator.remove();
                }
                adapter.notifyDataSetChanged();
                AppUtils.cacheMessages(getContext(), messages);
            } else {
                Utils.showLongToast(getContext(), R.string.failed_deleting_message);
            }
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();

        Utils.showLongToast(getContext(), message);
        Utils.LogE(message);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ib_delete_messages:
                StringBuilder ids = new StringBuilder();
                boolean firstId = true;// don't put comma before the first id
                for (int i = 0; i < messages.size(); i++) {
                    Message message = messages.get(i);
                    if (message.isSelected()) {
                        if (firstId) { // don't put comma before the first id
                            ids.append(message.getId());
                            firstId = false;
                        } else {
                            ids.append(",").append(message.getId());
                        }
                    }
                }
                if (firstId) return; //no message selected
                progressDialog = DialogUtils.showProgressDialog(getActivity(), R.string.deleting_message);
                DriverRequests.messagesDelete(getActivity(), this, AppUtils.getCachedUser(getContext()).getAccessToken(), ids.toString());
                break;
            case R.id.ib_new_message:
                startActivity(new Intent(getActivity(), DriverCreateMessageActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        messages.get(position).setSeen(true);
        adapter.notifyItemChanged(position);
        AppUtils.cacheMessages(getContext(), messages);
        Intent intent = new Intent(getActivity(), DriverMessageDetailsActivity.class);
        intent.putExtra("message", messages.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, Const.REQUEST_MESSAGE_DETAILS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_MESSAGE_DETAILS && resultCode == Activity.RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                messages.remove(position);
                adapter.notifyDataSetChanged();
                AppUtils.cacheMessages(getContext(), messages);
            }
        }
    }
}
