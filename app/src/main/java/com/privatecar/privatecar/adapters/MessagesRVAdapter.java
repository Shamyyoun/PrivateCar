package com.privatecar.privatecar.adapters;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverMessageDetails;
import com.privatecar.privatecar.models.entities.Message;
import com.privatecar.privatecar.utils.AppUtils;

import java.util.List;

/**
 * Created by basim on 14/2/16.
 */
public class MessagesRVAdapter extends RecyclerView.Adapter<MessagesRVAdapter.ViewHolder> {

    private List<Message> messages;

    public MessagesRVAdapter(List<Message> messages) {
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
        holder.tvMessage.setText(message.getMessage());
        holder.tvDate.setText(message.getCreatedAt());
        if (!message.isSeen()) {
            holder.tvMessage.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvMessage.setTypeface(null, Typeface.NORMAL);
        }
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DriverMessageDetails.class);
                    intent.putExtra("message", messages.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                    messages.get(getAdapterPosition()).setSeen(true);
                    notifyItemChanged(getAdapterPosition());
                    AppUtils.cacheMessages(v.getContext(), messages);
                }
            });

            cbMessage = (CheckBox) itemView.findViewById(R.id.cb_message);
            cbMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    messages.get(getAdapterPosition()).setSelected(isChecked);
                }
            });

            tvMessage = (TextView) itemView.findViewById(R.id.tv_message_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
