package com.privatecar.privatecar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by basim on 19/2/16.
 */
public class DriverDocumentsRVAdapter extends RecyclerView.Adapter<DriverDocumentsRVAdapter.DocumentHolder> {

    Context ctx;
    ArrayList<String> documents;


    public DriverDocumentsRVAdapter(Context ctx, ArrayList<String> documents) {
        this.ctx = ctx;
        this.documents = documents;
    }

    @Override
    public DocumentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View document = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_document_rv_item, parent, false);
        return new DocumentHolder(document);
    }

    @Override
    public void onBindViewHolder(DocumentHolder holder, int position) {
        Picasso.with(ctx).load(documents.get(position)).into(holder.ivDocument);
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    static class DocumentHolder extends RecyclerView.ViewHolder {
        ImageView ivDocument;

        public DocumentHolder(View itemView) {
            super(itemView);

            ivDocument = (ImageView) itemView;
            ivDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showToast(ivDocument.getContext(), "position: " + getAdapterPosition());
                }
            });
        }
    }


}
