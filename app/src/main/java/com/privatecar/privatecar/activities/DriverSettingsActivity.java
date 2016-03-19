package com.privatecar.privatecar.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.DriverAccountDetails;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.BitmapUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DriverSettingsActivity extends BasicBackActivity {
    private View layoutChangePassword, layoutChangeLanguage;
    private ImageButton ibUserPhoto;
    private TextView tvLanguage, tvName, tvMobile, tvEmail;
    AlertDialog.Builder builder;
    File imageUserPhoto, imageUserPhotoCropped;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        // init views
        layoutChangePassword = findViewById(R.id.layout_change_password);
        layoutChangePassword.setOnClickListener(this);
        layoutChangeLanguage = findViewById(R.id.layout_change_language);
        layoutChangeLanguage.setOnClickListener(this);

        tvLanguage = (TextView) findViewById(R.id.tv_language);
        String language = Utils.getAppLanguage();
        if (language.equals("ar")) {
            tvLanguage.setText(R.string.arabic);
        } else {
            tvLanguage.setText(R.string.english);
        }

        DriverAccountDetails details = AppUtils.getCachedUser(getApplicationContext()).getDriverAccountDetails();

        tvName = (TextView) findViewById(R.id.tv_driver_name);
        tvName.setText(details.getFullname());
        tvMobile = (TextView) findViewById(R.id.tv_mobile);
//        tvMobile.setText(details.); //TODO: set this value
        tvEmail = (TextView) findViewById(R.id.tv_email);
//        tvEmail.setText(details.); //TODO: set this value

        ibUserPhoto = (ImageButton) findViewById(R.id.ib_user_photo);
        String photoUrl = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_BASE_URL) + File.separator + details.getPersonalPhoto();
        Utils.LogE(photoUrl);
        Picasso.with(this).load(photoUrl).error(R.drawable.add_placeholder).into(ibUserPhoto);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_change_password:
                // open change password activity
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.layout_change_language:
                builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: //english
                                Utils.changeAppLocale(getApplicationContext(), "en");
                                Utils.cacheString(getApplicationContext(), Const.CACHE_LOCALE, "en");
                                break;
                            case 1: //arabic
                                Utils.changeAppLocale(getApplicationContext(), "ar");
                                Utils.cacheString(getApplicationContext(), Const.CACHE_LOCALE, "ar");
                                break;
                        }

                        Intent intent = new Intent(DriverSettingsActivity.this, DriverHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                builder.show();

                break;
            case R.id.ib_user_photo:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.photo_sources_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: //Gallery
                                requestGalleryImage(Const.REQUEST_GALLERY_USER_PHOTO);
                                break;
                            case 1: //Camera
                                requestCameraImage(Const.REQUEST_CAMERA_USER_PHOTO);
                                break;
                        }
                    }
                });
                builder.show();

                break;
        }
    }


    private void requestCameraImage(int requestCode) {
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile = null;
        if (captureImageIntent.resolveActivity(getPackageManager()) != null) {
            switch (requestCode) {
                case Const.REQUEST_CAMERA_USER_PHOTO:
                    imageUserPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_USER_PHOTO);
                    tempFile = imageUserPhoto;
                    break;
            }

            if (tempFile != null) {
                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(captureImageIntent, requestCode);
            }
        }
    }

    private void requestGalleryImage(int requestCode) {
        Crop.pickImage(this, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //User Photo
        if (requestCode == Const.REQUEST_GALLERY_USER_PHOTO && resultCode == RESULT_OK) {
            imageUserPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_USER_PHOTO);
            if (imageUserPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageUserPhotoCropped)).asSquare().start(this, Const.REQUEST_CROP_USER_PHOTO);
        } else if (requestCode == Const.REQUEST_CAMERA_USER_PHOTO && resultCode == RESULT_OK) {
            imageUserPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_USER_PHOTO);
            if (imageUserPhotoCropped != null && imageUserPhoto != null)
                Crop.of(Uri.fromFile(imageUserPhoto), Uri.fromFile(imageUserPhotoCropped)).asSquare().start(this, Const.REQUEST_CROP_USER_PHOTO);
        } else if (requestCode == Const.REQUEST_CROP_USER_PHOTO && resultCode == RESULT_OK) {
            BitmapUtils.resizeBitmap(imageUserPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE_USER, Const.IMAGE_SIZE_USER);
            Glide.with(this).load(imageUserPhotoCropped).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ibUserPhoto);

            progressDialog = DialogUtils.showProgressDialog(this, R.string.loading);
            CommonRequests.changePhoto(this, listener, AppUtils.getCachedUser(getApplicationContext()).getAccessToken(), imageUserPhotoCropped);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("imageUserPhoto", imageUserPhoto);
        outState.putSerializable("imageUserPhotoCropped", imageUserPhotoCropped);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        imageUserPhoto = (File) savedInstanceState.getSerializable("imageUserPhoto");
        imageUserPhotoCropped = (File) savedInstanceState.getSerializable("imageUserPhotoCropped");

        if (imageUserPhotoCropped != null) {
            Glide.with(this).load(imageUserPhotoCropped).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ibUserPhoto);
        }

    }


    private RequestListener<GeneralResponse> listener = new RequestListener<GeneralResponse>() {
        @Override
        public void onSuccess(GeneralResponse response, String apiName) {
            progressDialog.dismiss();
            if (response.isSuccess()) {
                Utils.showLongToast(getApplicationContext(), R.string.success);
            }
        }

        @Override
        public void onFail(String message, String apiName) {
            progressDialog.dismiss();
            Utils.LogE(message);
            Utils.showLongToast(getApplicationContext(), message);
        }
    };
}
