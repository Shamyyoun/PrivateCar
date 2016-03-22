package com.privatecar.privatecar.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverDocumentDetailsActivity;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.wrappers.SerializableListWrapper;
import com.privatecar.privatecar.utils.AppUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by basim on 19/2/16.
 */
public class DriverDocumentsRVAdapter extends RecyclerView.Adapter<DriverDocumentsRVAdapter.DocumentHolder> {
    Context ctx;
    List<String> documents;

    public DriverDocumentsRVAdapter(Context ctx, List<String> documents) {
        this.ctx = ctx;
        this.documents = documents;
    }

    @Override
    public DocumentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View document = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_document_rv_item, parent, false);
        return new DocumentHolder(document, documents);
    }

    @Override
    public void onBindViewHolder(final DocumentHolder holder, int position) {
        // reset views
        holder.progressBar.setVisibility(View.VISIBLE);

        // load image with picasso
        String documentUrl = AppUtils.getConfigValue(ctx, Config.KEY_BASE_URL) + File.separator + documents.get(position);
        Picasso.with(ctx).load(documentUrl).error(R.drawable.default_image).into(holder.ivDocument, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    static class DocumentHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ImageView ivDocument;

        public DocumentHolder(View itemView, final List<String> documents) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            ivDocument = (ImageView) itemView.findViewById(R.id.iv_document);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SerializableListWrapper<String> documentsWrapper = new SerializableListWrapper<String>(documents);
                    Intent intent = new Intent(ivDocument.getContext(), DriverDocumentDetailsActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("documents", documentsWrapper);
                    ivDocument.getContext().startActivity(intent);
                }
            });
        }
    }
}
