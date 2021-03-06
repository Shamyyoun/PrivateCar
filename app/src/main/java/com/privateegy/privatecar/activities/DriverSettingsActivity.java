package com.privateegy.privatecar.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.dialogs.ChangeLanguageDialog;
import com.privateegy.privatecar.models.entities.Config;
import com.privateegy.privatecar.models.entities.DriverAccountDetails;
import com.privateegy.privatecar.models.enums.Language;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.CommonRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.BitmapUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.PermissionUtil;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DriverSettingsActivity extends BasicBackActivity {
    private View layoutChangePassword, layoutChangeLanguage, layoutTellFriends, layoutCustomerService;
    private ImageButton ibUserPhoto;
    private TextView tvLanguage, tvName, tvMobile, tvEmail;
    File imageUserPhoto, imageUserPhotoCropped;

    private ChangeLanguageDialog languageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        // init views
        layoutChangePassword = findViewById(R.id.layout_change_password);
        layoutChangePassword.setOnClickListener(this);
        layoutChangeLanguage = findViewById(R.id.layout_change_language);
        layoutChangeLanguage.setOnClickListener(this);
        layoutTellFriends = findViewById(R.id.layout_tell_friends);
        layoutTellFriends.setOnClickListener(this);
        layoutCustomerService = findViewById(R.id.layout_customer_service);
        layoutCustomerService.setOnClickListener(this);


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
        tvMobile.setText(details.getMobile());
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvEmail.setText(details.getEmail());

        ibUserPhoto = (ImageButton) findViewById(R.id.ib_user_photo);
        String photoUrl = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_BASE_URL) + File.separator + details.getPersonalPhoto();
        Utils.LogE(photoUrl);
        Picasso.with(this).load(photoUrl).error(R.drawable.def_user_photo).placeholder(R.drawable.def_user_photo).into(ibUserPhoto);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_change_password:
                // open change password activity
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;

            case R.id.layout_change_language:
                onChangeLanguage();
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
            case R.id.layout_tell_friends:
                // open invite friends activity
                startActivity(new Intent(this, CustomerInviteFriendsActivity.class));
                break;
            case R.id.layout_customer_service:
                // check call permission
                if (PermissionUtil.isGranted(this, Manifest.permission.CALL_PHONE)) {
                    // show call customer service dialog
                    AppUtils.showCallDriverServiceDialog(this);
                } else {
                    // not granted
                    // request the permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Const.PERM_REQ_CALL);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Const.PERM_REQ_CALL:
                // check if granted
                if (PermissionUtil.isAllGranted(grantResults)) {
                    // granted
                    // show call customer service dialog
                    AppUtils.showCallDriverServiceDialog(this);
                } else {
                    // show msg
                    Utils.showShortToast(this, R.string.we_need_call_permission_to_call_customer_service);
                }
                break;
        }
    }

    private void onChangeLanguage() {
        languageDialog = new ChangeLanguageDialog(this, new ChangeLanguageDialog.OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                // change app locale and cache it
                Utils.changeAppLocale(DriverSettingsActivity.this, language.getValue());
                Utils.cacheString(DriverSettingsActivity.this, Const.CACHE_LOCALE, language.getValue());

                // finish and start driver home activity again
                Intent intent = new Intent(DriverSettingsActivity.this, DriverHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        languageDialog.show();
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
