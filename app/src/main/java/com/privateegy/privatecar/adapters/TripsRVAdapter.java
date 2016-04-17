package com.privateegy.privatecar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.responses.CustomerTrip;
import com.privateegy.privatecar.utils.DateUtil;

import java.util.List;

/**
 * Created by basim on 20/2/16.
 */
public class TripsRVAdapter extends RecyclerView.Adapter<TripsRVAdapter.RideViewHolder> {
    private Context context;
    private List<CustomerTrip> trips;

    public TripsRVAdapter(Context context, List<CustomerTrip> trips) {
        this.context = context;
        this.trips = trips;
    }

    @Override
    public RideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_rides_item, parent, false);
        return new RideViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RideViewHolder holder, int position) {
        CustomerTrip trip = trips.get(position);

        holder.tvRideNumber.setText(String.valueOf(trip.getCode()));
        holder.tvPickupAddress.setText(trip.getPickupaddress());
        holder.tvDropOffAddress.setText(trip.getDestinationaddress());
        holder.tvPrice.setText(trip.getFare() + " " + context.getString(R.string.currency));
        String date = DateUtil.formatDate(trip.getDate(), "yyyy-MM-dd hh:mm:ss", "d MMM yyyy");
        holder.tvDate.setText("" + date);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }


    public static class RideViewHolder extends RecyclerView.ViewHolder {

        TextView tvRideNumber;
        TextView tvPickupAddress;
        TextView tvDropOffAddress;
        TextView tvPrice;
        TextView tvDate;

        public RideViewHolder(View itemView) {
            super(itemView);

            tvRideNumber = (TextView) itemView.findViewById(R.id.tv_ride_number);
            tvPickupAddress = (TextView) itemView.findViewById(R.id.tv_pickup_address);
            tvDropOffAddress = (TextView) itemView.findViewById(R.id.tv_drop_off_address);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
