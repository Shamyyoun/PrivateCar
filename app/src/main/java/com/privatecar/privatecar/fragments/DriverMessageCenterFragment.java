package com.privatecar.privatecar.fragments;


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

import java.util.ArrayList;

public class DriverMessageCenterFragment extends BaseFragment {


    CheckBox cbMessages;
    ArrayList<Message> messages = new ArrayList<>();
    RecyclerView rvMessages;
    MessagesRVAdapter adapter;

    public DriverMessageCenterFragment() {
        // Required empty public constructor
    }

    private void fillDummyMessages() {
        for (int i = 0; i < 40; i++) {
            Message message = new Message();
            message.setTitle("This is a message title that you must read, numbered " + i);
            message.setBody("This is a message body that you must read, numbered " + i);
            message.setDate("20-10-2015");

            messages.add(message);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_message_center, container, false);

        fillDummyMessages();

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

}
