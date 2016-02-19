package com.privatecar.privatecar.fragments;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.privatecar.privatecar.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageSlideFragment extends BaseFragment {


    ImageView ivImage;
    ProgressBar progressBar;
    String url;
    PhotoViewAttacher attacher;

    public ImageSlideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_slide, container, false);
        ivImage = (ImageView) view.findViewById(R.id.iv_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN); // coloring the progressbar white

        url = getArguments().getString("url");

        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                ivImage.setImageBitmap(resource);
                attacher = new PhotoViewAttacher(ivImage);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                ivImage.setImageDrawable(errorDrawable);
                progressBar.setVisibility(View.GONE);
            }
        };

        if (url != null) {
            Glide.with(getActivity()).load(url).asBitmap().fitCenter().into(target);
        }


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (attacher != null)
            attacher.cleanup();
    }

}
