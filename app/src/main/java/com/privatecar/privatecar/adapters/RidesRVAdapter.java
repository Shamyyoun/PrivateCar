package com.privatecar.privatecar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Ride;

import java.util.ArrayList;

/**
 * Created by basim on 20/2/16.
 *
 */
public class RidesRVAdapter extends RecyclerView.Adapter<RidesRVAdapter.RideViewHolder> {

    ArrayList<Ride> rides;

    public RidesRVAdapter(ArrayList<Ride> rides) {
        this.rides = rides;
    }

    @Override
    public RideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_rides_item, parent, false);
        return new RideViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RideViewHolder holder, int position) {
        Ride ride = rides.get(position);

        holder.tvRideNumber.setText(String.valueOf(ride.getRideNumber()));
        holder.tvReceiptNumber.setText(String.valueOf(ride.getReceiptNumber()));
        holder.tvPickupAddress.setText(ride.getPickupAddress());
        holder.tvDropOffAddress.setText(ride.getDropOffAddress());
        holder.tvPrice.setText(String.valueOf(ride.getPrice()));
        holder.tvDate.setText(ride.getDate());

    }

    @Override
    public int getItemCount() {
        return rides.size();
    }


    public static class RideViewHolder extends RecyclerView.ViewHolder {

        TextView tvRideNumber;
        TextView tvReceiptNumber;
        TextView tvPickupAddress;
        TextView tvDropOffAddress;
        TextView tvPrice;
        TextView tvDate;

        public RideViewHolder(View itemView) {
            super(itemView);

            tvRideNumber = (TextView) itemView.findViewById(R.id.tv_ride_number);
            tvReceiptNumber = (TextView) itemView.findViewById(R.id.tv_receipt_number);
            tvPickupAddress = (TextView) itemView.findViewById(R.id.tv_pickup_address);
            tvDropOffAddress = (TextView) itemView.findViewById(R.id.tv_drop_off_address);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
