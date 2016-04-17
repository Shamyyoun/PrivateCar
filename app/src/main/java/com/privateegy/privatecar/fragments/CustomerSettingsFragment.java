package com.privateegy.privatecar.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.activities.AnonymousHomeActivity;
import com.privateegy.privatecar.activities.ChangePasswordActivity;
import com.privateegy.privatecar.activities.CustomerAboutPrivateActivity;
import com.privateegy.privatecar.activities.CustomerAddCreditCardActivity;
import com.privateegy.privatecar.activities.CustomerAddPromoCodeActivity;
import com.privateegy.privatecar.activities.CustomerHomeActivity;
import com.privateegy.privatecar.activities.CustomerInviteFriendsActivity;
import com.privateegy.privatecar.dialogs.ChangeLanguageDialog;
import com.privateegy.privatecar.models.entities.Config;
import com.privateegy.privatecar.models.entities.User;
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
import java.util.List;
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
                                // check storage permission
                                if (PermissionUtil.isGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    // granted
                                    requestGalleryImage(Const.REQUEST_GALLERY_USER_PHOTO);
                                } else {
                                    // not granted
                                    // request the permission
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PERM_REQ_STORAGE);
                                }
                                break;
                            case 1: //Camera
                                // check the permissions
                                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                List<String> notGrantedPermissions = PermissionUtil.getNotGranted(activity, permissions);

                                // check not granted
                                if (notGrantedPermissions.size() == 0) {
                                    Log.e("PERMISSIONS", "All Granted");
                                    // all permissions are granted
                                    requestCameraImage(Const.REQUEST_CAMERA_USER_PHOTO);
                                } else {
                                    // request other permissions
                                    String[] notGrantedPermissionsArr = new String[notGrantedPermissions.size()];
                                    notGrantedPermissionsArr = notGrantedPermissions.toArray(notGrantedPermissionsArr);
                                    notGrantedPermissions.toArray(permissions);

                                    Log.e("PERMISSIONS", "Not all granted");
                                    for (int i = 0; i < notGrantedPermissions.size(); i++) {
                                        Log.e("PERM", notGrantedPermissions.get(i));
                                    }

                                    requestPermissions(notGrantedPermissionsArr, Const.PERM_REQ_CAMERA);
                                }
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
                // check call permission
                if (PermissionUtil.isGranted(activity, Manifest.permission.CALL_PHONE)) {
                    // show call customer service dialog
                    AppUtils.showCallCustomerServiceDialog(activity);
                } else {
                    // not granted
                    // request the permission
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, Const.PERM_REQ_CALL);
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Const.PERM_REQ_CALL:
                // check if granted
                if (PermissionUtil.isAllGranted(grantResults)) {
                    // granted
                    // show call customer service dialog
                    AppUtils.showCallCustomerServiceDialog(activity);
                } else {
                    // show msg
                    Utils.showShortToast(activity, R.string.we_need_call_permission_to_call_customer_service);
                }
                break;

            case Const.PERM_REQ_STORAGE:
                // check if granted
                if (PermissionUtil.isAllGranted(grantResults)) {
                    // granted
                    // request gallery image
                    requestGalleryImage(Const.REQUEST_GALLERY_USER_PHOTO);
                } else {
                    // show msg
                    Utils.showShortToast(activity, R.string.we_need_storage_permission_to_choose_your_profile_photo);
                }
                break;

            case Const.PERM_REQ_CAMERA:
                // check if granted
                if (PermissionUtil.isAllGranted(grantResults)) {
                    // granted
                    // request camera image
                    requestCameraImage(Const.REQUEST_CAMERA_USER_PHOTO);
                } else {
                    // show msg
                    Utils.showShortToast(activity, R.string.we_need_camera_permission_to_capture_your_profile_photo);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
