package com.privateegy.privatecar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.PrivateCarPlace;
import com.privateegy.privatecar.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 2/8/2015.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private Context context;
    private List<PrivateCarPlace> data;
    private int layoutResourceId;
    private OnItemClickListener onItemClickListener;

    public PlacesAdapter(Context context, List<PrivateCarPlace> data, int layoutResourceId) {
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PrivateCarPlace place = data.get(position);

        // set data
        holder.textName.setText(place.getName());
        if (!Utils.isEmpty(place.getAddress())) {
            holder.textAddress.setVisibility(View.VISIBLE);
            holder.textAddress.setText(place.getAddress());
        } else {
            holder.textAddress.setVisibility(View.GONE);
        }

        if (place.isMarkerLocation()) {
            // show pin image
            holder.timeLayout.setVisibility(View.GONE);
            holder.imagePin.setVisibility(View.VISIBLE);
        } else {
            // show time layout
//            holder.textTime.setText("" + place.getTime() + " " + context.getString(R.string.min));
//            holder.timeLayout.setVisibility(View.VISIBLE);
            holder.imagePin.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textName;
        public TextView textAddress;
        public View timeLayout;
        public TextView textTime;
        public ImageView imagePin;

        public ViewHolder(View v) {
            super(v);
            textName = (TextView) v.findViewById(R.id.tv_place_title);
            textAddress = (TextView) v.findViewById(R.id.tv_place_address);
            timeLayout = v.findViewById(R.id.layout_time);
            textTime = (TextView) v.findViewById(R.id.tv_time);
            imagePin = (ImageView) v.findViewById(R.id.iv_pin);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}