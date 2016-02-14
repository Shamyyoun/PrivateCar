package com.privatecar.privatecar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Message;

import java.util.ArrayList;

/**
 * Created by basim on 14/2/16.
 */
public class RVMessagesAdapter extends RecyclerView.Adapter<RVMessagesAdapter.ViewHolder> {

    private ArrayList<Message> messages;

    public RVMessagesAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View messageItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_message_item, parent, false);
        return new ViewHolder(messageItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.cbMessage.setChecked(message.isSelected());
        holder.tvMessage.setText(message.getContent());
        holder.tvDate.setText(message.getDate());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbMessage;
        public TextView tvMessage;
        public TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            cbMessage = (CheckBox) itemView.findViewById(R.id.cb_message);
            cbMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    messages.get(getAdapterPosition()).setSelected(isChecked);
                }
            });

            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
