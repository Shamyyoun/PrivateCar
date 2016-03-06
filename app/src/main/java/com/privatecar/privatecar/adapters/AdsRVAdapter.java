package com.privatecar.privatecar.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Ad;
import com.privatecar.privatecar.utils.Utils;

import java.util.List;

/**
 * Created by basim on 19/2/16.
 */
public class AdsRVAdapter extends RecyclerView.Adapter<AdsRVAdapter.AdHolder> {
    Context ctx;
    List<Ad> ads;


    public AdsRVAdapter(Context ctx, List<Ad> ads) {
        this.ctx = ctx;
        this.ads = ads;
    }

    @Override
    public AdHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View document = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_ads_item, parent, false);
        return new AdHolder(document);
    }

    @Override
    public void onBindViewHolder(AdHolder holder, int position) {
        final Ad ad = ads.get(position);

        // init views with ad data and click listener
        holder.tvTitle.setText(ad.getTitle());
        holder.tvDesc.setText(ad.getDescription());
        holder.btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open link in the browser
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Utils.formatUrl(ad.getLink())));
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    static class AdHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDesc;
        Button btnVisit;

        public AdHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            btnVisit = (Button) itemView.findViewById(R.id.btn_visit);
        }
    }


}
