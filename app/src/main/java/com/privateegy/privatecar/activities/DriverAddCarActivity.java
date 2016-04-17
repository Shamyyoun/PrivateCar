package com.privateegy.privatecar.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.BitmapUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;
import com.soundcloud.android.crop.Crop;

import java.io.File;

public class DriverAddCarActivity extends BasicBackActivity implements RequestListener<GeneralResponse> {
    private ImageButton ibCarPhoto;
    private ImageView ivCarPhotoValidation, ivCarLicenceFrontValidation, ivCarLicenceBackValidation;

    File imageCarPhoto, imageCarPhotoCropped;
    File imageCarLicenceFrontPhoto, imageCarLicenceFrontPhotoCropped;
    File imageCarLicenceBackPhoto, imageCarLicenceBackPhotoCropped;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_add_car);

        // init views
        ibCarPhoto = (ImageButton) findViewById(R.id.ib_car_photo);
        ivCarPhotoValidation = (ImageView) findViewById(R.id.iv_car_photo_validation);
        ivCarLicenceFrontValidation = (ImageView) findViewById(R.id.iv_car_licence_front_validation);
        ivCarLicenceBackValidation = (ImageView) findViewById(R.id.iv_car_licence_back_validation);

        // add click listeners
        ibCarPhoto.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Car Photo
        if (requestCode == Const.REQUEST_GALLERY_CAR_PHOTO && resultCode == RESULT_OK) {
            imageCarPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_CAR_PHOTO);
            if (imageCarPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageCarPhotoCropped)).start(this, Const.REQUEST_CROP_CAR_PHOTO);
        } else if (requestCode == Const.REQUEST_CAMERA_CAR_PHOTO && resultCode == RESULT_OK) {
            imageCarPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_CAR_PHOTO);
            if (imageCarPhotoCropped != null && imageCarPhoto != null)
                Crop.of(Uri.fromFile(imageCarPhoto), Uri.fromFile(imageCarPhotoCropped)).start(this, Const.REQUEST_CROP_CAR_PHOTO);
        } else if (requestCode == Const.REQUEST_CROP_CAR_PHOTO && resultCode == RESULT_OK) {
            BitmapUtils.resizeBitmap(imageCarPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE, Const.IMAGE_SIZE);
            Glide.with(this).load(imageCarPhotoCropped).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ibCarPhoto);
            ivCarPhotoValidation.setVisibility(View.GONE);
        }


        //Car Licence Front Photo
        else if (requestCode == Const.REQUEST_GALLERY_CAR_LICENCE_FRONT && resultCode == RESULT_OK) {
            imageCarLicenceFrontPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_CAR_LICENCE_FRONT);
            if (imageCarLicenceFrontPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageCarLicenceFrontPhotoCropped)).start(this, Const.REQUEST_CROP_CAR_LICENCE_FRONT);
        } else if (requestCode == Const.REQUEST_CAMERA_CAR_LICENCE_FRONT && resultCode == RESULT_OK) {
            imageCarLicenceFrontPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_CAR_LICENCE_FRONT);
            if (imageCarLicenceFrontPhotoCropped != null && imageCarLicenceFrontPhoto != null)
                Crop.of(Uri.fromFile(imageCarLicenceFrontPhoto), Uri.fromFile(imageCarLicenceFrontPhotoCropped)).start(this, Const.REQUEST_CROP_CAR_LICENCE_FRONT);
        } else if (requestCode == Const.REQUEST_CROP_CAR_LICENCE_FRONT && resultCode == RESULT_OK) {
            BitmapUtils.resizeBitmap(imageCarLicenceFrontPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE, Const.IMAGE_SIZE);
            ivCarLicenceFrontValidation.setImageResource(R.drawable.success_icon);
        }

        //Car Licence Back Photo
        else if (requestCode == Const.REQUEST_GALLERY_CAR_LICENCE_BACK && resultCode == RESULT_OK) {
            imageCarLicenceBackPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_CAR_LICENCE_BACK);
            if (imageCarLicenceBackPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageCarLicenceBackPhotoCropped)).start(this, Const.REQUEST_CROP_CAR_LICENCE_BACK);
        } else if (requestCode == Const.REQUEST_CAMERA_CAR_LICENCE_BACK && resultCode == RESULT_OK) {
            imageCarLicenceBackPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_CAR_LICENCE_BACK);
            if (imageCarLicenceBackPhotoCropped != null && imageCarLicenceBackPhoto != null)
                Crop.of(Uri.fromFile(imageCarLicenceBackPhoto), Uri.fromFile(imageCarLicenceBackPhotoCropped)).start(this, Const.REQUEST_CROP_CAR_LICENCE_BACK);
        } else if (requestCode == Const.REQUEST_CROP_CAR_LICENCE_BACK && resultCode == RESULT_OK) {
            BitmapUtils.resizeBitmap(imageCarLicenceBackPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE, Const.IMAGE_SIZE);
            ivCarLicenceBackValidation.setImageResource(R.drawable.success_icon);
        }

    }

    public void onClick(View view) {
        AlertDialog.Builder builder;

        switch (view.getId()) {
            case R.id.ib_car_photo:
                builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.photo_sources_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: //Gallery
                                requestGalleryImage(Const.REQUEST_GALLERY_CAR_PHOTO);
                                break;
                            case 1: //Camera
                                requestCameraImage(Const.REQUEST_CAMERA_CAR_PHOTO);
                                break;
                        }
                    }
                });
                builder.show();
                break;

            case R.id.ib_car_licence_front_gallery:
                requestGalleryImage(Const.REQUEST_GALLERY_CAR_LICENCE_FRONT);
                break;

            case R.id.ib_car_licence_front_camera:
                requestCameraImage(Const.REQUEST_CAMERA_CAR_LICENCE_FRONT);
                break;

            case R.id.ib_car_licence_back_gallery:
                requestGalleryImage(Const.REQUEST_GALLERY_CAR_LICENCE_BACK);
                break;

            case R.id.ib_car_licence_back_camera:
                requestCameraImage(Const.REQUEST_CAMERA_CAR_LICENCE_BACK);
                break;

            case R.id.btn_add_car:
                addCar();
                break;
        }
    }

    private void requestCameraImage(int requestCode) {
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile = null;
        if (captureImageIntent.resolveActivity(getPackageManager()) != null) {
            switch (requestCode) {
                case Const.REQUEST_CAMERA_CAR_PHOTO:
                    imageCarPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_CAR_PHOTO);
                    tempFile = imageCarPhoto;
                    break;
                case Const.REQUEST_CAMERA_CAR_LICENCE_FRONT:
                    imageCarLicenceFrontPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_CAR_LICENCE_FRONT);
                    tempFile = imageCarLicenceFrontPhoto;
                    break;
                case Const.REQUEST_CAMERA_CAR_LICENCE_BACK:
                    imageCarLicenceBackPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_CAR_LICENCE_BACK);
                    tempFile = imageCarLicenceBackPhoto;
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("imageCarPhoto", imageCarPhoto);
        outState.putSerializable("imageCarPhotoCropped", imageCarPhotoCropped);
        outState.putSerializable("imageCarLicenceFrontPhoto", imageCarLicenceFrontPhoto);
        outState.putSerializable("imageCarLicenceFrontPhotoCropped", imageCarLicenceFrontPhotoCropped);
        outState.putSerializable("imageCarLicenceBackPhoto", imageCarLicenceBackPhoto);
        outState.putSerializable("imageCarLicenceBackPhotoCropped", imageCarLicenceBackPhotoCropped);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        imageCarPhoto = (File) savedInstanceState.getSerializable("imageCarPhoto");
        imageCarPhotoCropped = (File) savedInstanceState.getSerializable("imageCarPhotoCropped");
        imageCarLicenceFrontPhoto = (File) savedInstanceState.getSerializable("imageCarLicenceFrontPhoto");
        imageCarLicenceFrontPhotoCropped = (File) savedInstanceState.getSerializable("imageCarLicenceFrontPhotoCropped");
        imageCarLicenceBackPhoto = (File) savedInstanceState.getSerializable("imageCarLicenceBackPhoto");
        imageCarLicenceBackPhotoCropped = (File) savedInstanceState.getSerializable("imageCarLicenceBackPhotoCropped");

        if (imageCarPhotoCropped != null) {
            Glide.with(this).load(imageCarPhotoCropped).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ibCarPhoto);
        }

        if (imageCarLicenceFrontPhotoCropped != null) {
            ivCarLicenceFrontValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageCarLicenceBackPhotoCropped != null) {
            ivCarLicenceBackValidation.setImageResource(R.drawable.success_icon);
        }

    }


    private boolean addCarValidation() {
        boolean valid = true;


        if (imageCarPhotoCropped == null) {
            ivCarPhotoValidation.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            ivCarPhotoValidation.setVisibility(View.GONE);
        }

        if (imageCarLicenceFrontPhotoCropped == null) {
            ivCarLicenceFrontValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivCarLicenceFrontValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageCarLicenceBackPhotoCropped == null) {
            ivCarLicenceBackValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivCarLicenceBackValidation.setImageResource(R.drawable.success_icon);
        }

        return valid;
    }

    private void addCar() {

        if (!addCarValidation()) //if the validation failed
            return;


        dialog = DialogUtils.showProgressDialog(this, R.string.adding_car, false);

        String accessToken = AppUtils.getCachedUser(getApplicationContext()).getAccessToken();
        DriverRequests.addCar(this, this, accessToken, imageCarPhotoCropped, imageCarLicenceFrontPhotoCropped, imageCarLicenceBackPhotoCropped);

    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        dialog.dismiss();

        if (response.isSuccess()) {
            startActivity(new Intent(this, DriverAddCarConfirmationActivity.class));
            this.onBackPressed();
        } else {
            if (response.getValidation() != null) {
                String validation = "";
                for (int i = 0; i < response.getValidation().size(); i++) {
                    if (i == 0)
                        validation += response.getValidation().get(i);
                    else
                        validation += "\n" + response.getValidation().get(i);

                }

                DialogUtils.showAlertDialog(this, validation, null);
            }
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        dialog.dismiss();
        Utils.showLongToast(this, message);
    }

}
