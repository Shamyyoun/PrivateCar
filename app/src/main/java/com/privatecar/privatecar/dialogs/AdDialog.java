package com.privatecar.privatecar.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Ad;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DateUtil;
import com.privatecar.privatecar.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class AdDialog extends ParentDialog implements View.OnClickListener {
    private Context context;
    private Ad ad;
    private TextView tvTitle;
    private ProgressBar progressBar;
    private ImageView ivImage;
    private TextView tvDesc;
    private Button btnClose;

    public AdDialog(Context context, Ad ad) {
        super(context);
        setContentView(R.layout.dialog_ad);

        // assign objects
        this.context = context;
        this.ad = ad;

        // init views
        tvTitle = (TextView) findViewById(R.id.tv_title);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        btnClose = (Button) findViewById(R.id.btn_close);

        // set data
        tvTitle.setText(ad.getTitle());
        if (Utils.isNullOrEmpty(ad.getDescription())) {
            tvDesc.setVisibility(View.GONE);
        } else {
            tvDesc.setText(ad.getDescription());
        }

        // load the image
        String imageUrl = AppUtils.getConfigValue(context, Config.KEY_BASE_URL) + File.separator + ad.getImage();
        Picasso.with(context).load(imageUrl).error(R.drawable.default_image).skipMemoryCache().into(ivImage, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
            }
        });

        // add click listeners
        ivImage.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                // open the add link
                Utils.openBrowser(context, ad.getLink());
                break;

            case R.id.btn_close:
                dismiss();
                break;
        }
    }
}
