package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.AnonymousHomeActivity;
import com.privatecar.privatecar.activities.ChangePasswordActivity;
import com.privatecar.privatecar.activities.CustomerAboutPrivateActivity;
import com.privatecar.privatecar.activities.CustomerAddCreditCardActivity;
import com.privatecar.privatecar.activities.CustomerAddPromoCodeActivity;
import com.privatecar.privatecar.activities.CustomerHomeActivity;
import com.privatecar.privatecar.activities.CustomerInviteFriendsActivity;
import com.privatecar.privatecar.dialogs.ChangeLanguageDialog;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.Language;
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
import java.util.Locale;

public class CustomerSettingsFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = CustomerSettingsFragment.class.getName();

    CustomerHomeActivity activity;
    View rootView;
    private ImageButton ibUserPhoto;
    private TextView tvName;
    private TextView tvMobile;
    private TextView tvEmail;
    private TextView tvLanguage;
    private View layoutChangePassword;
    private View layoutAddCreditCard;
    private View layoutPromoCode;
    private View layoutLanguage;
    private View layoutTellFriends;
    private View layoutAboutPrivate;
    private View layoutCustomerService;
    private View layoutSignOut;

    File imageUserPhoto, imageUserPhotoCropped;
    private ChangeLanguageDialog languageDialog;

    public CustomerSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (CustomerHomeActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.settingsFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_settings, container, false);

        // init views
        ibUserPhoto = (ImageButton) rootView.findViewById(R.id.ib_user_photo);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvMobile = (TextView) rootView.findViewById(R.id.tv_mobile);
        tvEmail = (TextView) rootView.findViewById(R.id.tv_email);
        tvLanguage = (TextView) rootView.findViewById(R.id.tv_language);
        layoutChangePassword = rootView.findViewById(R.id.layout_change_password);
        layoutAddCreditCard = rootView.findViewById(R.id.layout_add_credit_card);
        layoutPromoCode = rootView.findViewById(R.id.layout_promo_code);
        layoutLanguage = rootView.findViewById(R.id.layout_language);
        layoutTellFriends = rootView.findViewById(R.id.layout_tell_friends);
        layoutAboutPrivate = rootView.findViewById(R.id.layout_about_private);
        layoutCustomerService = rootView.findViewById(R.id.layout_customer_service);
        layoutSignOut = rootView.findViewById(R.id.layout_sign_out);

        // add click listeners
        ibUserPhoto.setOnClickListener(this);
        layoutChangePassword.setOnClickListener(this);
        layoutAddCreditCard.setOnClickListener(this);
        layoutPromoCode.setOnClickListener(this);
        layoutLanguage.setOnClickListener(this);
        layoutTellFriends.setOnClickListener(this);
        layoutAboutPrivate.setOnClickListener(this);
        layoutCustomerService.setOnClickListener(this);
        layoutSignOut.setOnClickListener(this);

        // update the ui
        User user = AppUtils.getCachedUser(activity);
        tvName.setText(user.getCustomerAccountDetails().getFullname());
        tvMobile.setText(user.getCustomerAccountDetails().getMobile());
        tvEmail.setText(user.getCustomerAccountDetails().getEmail());
        tvLanguage.setText(Utils.getAppLanguage().equals(Locale.ENGLISH.getLanguage()) ? R.string.english : R.string.arabic);

        // load user image
        String photoUrl = AppUtils.getConfigValue(activity, Config.KEY_BASE_URL) + File.separator + user.getCustomerAccountDetails().getPersonalPhoto();
        Picasso.with(activity).load(photoUrl).error(R.drawable.add_placeholder).placeholder(R.drawable.add_placeholder).into(ibUserPhoto);

        // check saved instance state
        if (savedInstanceState != null) {
            // update user photo if exists
            imageUserPhoto = (File) savedInstanceState.getSerializable("imageUserPhoto");
            imageUserPhotoCropped = (File) savedInstanceState.getSerializable("imageUserPhotoCropped");

            if (imageUserPhotoCropped != null) {
                Glide.with(this).load(imageUserPhotoCropped).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ibUserPhoto);
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_user_photo:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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

            case R.id.layout_change_password:
                onChangePassword();
                break;

            case R.id.layout_add_credit_card:
                // open add credit card activity
                startActivity(new Intent(activity, CustomerAddCreditCardActivity.class));
                break;

            case R.id.layout_promo_code:
                // open add promo code activity
                startActivity(new Intent(activity, CustomerAddPromoCodeActivity.class));
                break;

            case R.id.layout_language:
                onChangeLanguage();
                break;

            case R.id.layout_tell_friends:
                // open invite friends activity
                startActivity(new Intent(activity, CustomerInviteFriendsActivity.class));
                break;

            case R.id.layout_about_private:
                // open about private activity
                startActivity(new Intent(activity, CustomerAboutPrivateActivity.class));
                break;

            case R.id.layout_customer_service:
                // show call customer service dialog
                AppUtils.showCallCustomerServiceDialog(activity);
                break;

            case R.id.layout_sign_out:
                // clear cache and goto anonymous home activity
                AppUtils.clearCache(activity);
                Intent intent = new Intent(activity, AnonymousHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

        }
    }

    private void requestCameraImage(int requestCode) {
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile = null;
        if (captureImageIntent.resolveActivity(activity.getPackageManager()) != null) {
            switch (requestCode) {
                case Const.REQUEST_CAMERA_USER_PHOTO:
                    imageUserPhoto = BitmapUtils.createImagePath(activity, Const.FILE_NAME_USER_PHOTO);
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
        Crop.pickImage(activity, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO should test this action when the activity recreated
        //User Photo
        if (requestCode == Const.REQUEST_GALLERY_USER_PHOTO && resultCode == Activity.RESULT_OK) {
            imageUserPhotoCropped = BitmapUtils.createImagePath(activity, Const.FILE_NAME_CROPPED_USER_PHOTO);
            if (imageUserPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageUserPhotoCropped)).asSquare().start(activity, Const.REQUEST_CROP_USER_PHOTO);
        } else if (requestCode == Const.REQUEST_CAMERA_USER_PHOTO && resultCode == Activity.RESULT_OK) {
            imageUserPhotoCropped = BitmapUtils.createImagePath(activity, Const.FILE_NAME_CROPPED_USER_PHOTO);
            if (imageUserPhotoCropped != null && imageUserPhoto != null)
                Crop.of(Uri.fromFile(imageUserPhoto), Uri.fromFile(imageUserPhotoCropped)).asSquare().start(activity, Const.REQUEST_CROP_USER_PHOTO);
        } else if (requestCode == Const.REQUEST_CROP_USER_PHOTO && resultCode == Activity.RESULT_OK) {
            BitmapUtils.resizeBitmap(imageUserPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE_USER, Const.IMAGE_SIZE_USER);
            Glide.with(this).load(imageUserPhotoCropped).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ibUserPhoto);
            activity.updatePersonalPhoto(imageUserPhotoCropped);

            progressDialog = DialogUtils.showProgressDialog(activity, R.string.loading);
            CommonRequests.changePhoto(activity, listener, AppUtils.getCachedUser(activity).getAccessToken(), imageUserPhotoCropped);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("imageUserPhoto", imageUserPhoto);
        outState.putSerializable("imageUserPhotoCropped", imageUserPhotoCropped);
    }

    private RequestListener<GeneralResponse> listener = new RequestListener<GeneralResponse>() {
        @Override
        public void onSuccess(GeneralResponse response, String apiName) {
            progressDialog.dismiss();
            if (response.isSuccess()) {
                Utils.showLongToast(activity, R.string.success);
            }
        }

        @Override
        public void onFail(String message, String apiName) {
            progressDialog.dismiss();
            Utils.LogE(message);
            Utils.showLongToast(activity, message);
        }
    };

    private void onChangePassword() {
        // check the user grant type
        User user = AppUtils.getCachedUser(activity);
        if (Utils.isNullOrEmpty(user.getUserName())) {
            // this is a social user
            // show msg
            Utils.showLongToast(activity, R.string.social_users_cant_change_password);
        } else {
            // open change password activity
            startActivity(new Intent(activity, ChangePasswordActivity.class));
        }
    }

    private void onChangeLanguage() {
        languageDialog = new ChangeLanguageDialog(activity, new ChangeLanguageDialog.OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                // change app locale and cache it
                Utils.changeAppLocale(activity, language.getValue());
                Utils.cacheString(activity, Const.CACHE_LOCALE, language.getValue());

                // finish and start customer home activity again
                Intent intent = new Intent(activity, CustomerHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        languageDialog.show();
    }

    @Override
    public void onDestroy() {
        activity.settingsFragment = null;
        super.onDestroy();
    }
}
