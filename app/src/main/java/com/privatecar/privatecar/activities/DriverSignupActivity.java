package com.privatecar.privatecar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.CountryAdapter;
import com.privatecar.privatecar.utils.BitmapUtils;
import com.privatecar.privatecar.utils.Utils;
import com.soundcloud.android.crop.Crop;

import java.io.File;

public class DriverSignupActivity extends BasicBackActivity {

    private EditText etFirstName, etLastName, etMobile, etEmail;
    private Spinner spinner;
    private ImageButton ibUserPhoto, ibCarPhoto;
    private ImageView ivUserPhotoValidation, ivCarPhotoValidation, ivIdFrontValidation, ivIdBackValidation, ivDriverLicenceFrontValidation, ivDriverLicenceBackValidation, ivCarLicenceFrontValidation, ivCarLicenceBackValidation;

    File imageUserPhoto, imageUserPhotoCropped;
    File imageCarPhoto, imageCarPhotoCropped;
    File imageIdFrontPhoto, imageIdFrontPhotoCropped;
    File imageIdBackPhoto, imageIdBackPhotoCropped;
    File imageDriverLicenceFrontPhoto, imageDriverLicenceFrontPhotoCropped;
    File imageDriverLicenceBackPhoto, imageDriverLicenceBackPhotoCropped;
    File imageCarLicenceFrontPhoto, imageCarLicenceFrontPhotoCropped;
    File imageCarLicenceBackPhoto, imageCarLicenceBackPhotoCropped;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup);

        spinner = (Spinner) findViewById(R.id.spinner_countries);
        CountryAdapter adapter = new CountryAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(61); //set egypt the default

        ibUserPhoto = (ImageButton) findViewById(R.id.ib_user_photo);
        ibCarPhoto = (ImageButton) findViewById(R.id.ib_car_photo);

        ivUserPhotoValidation = (ImageView) findViewById(R.id.iv_user_photo_validation);
        ivCarPhotoValidation = (ImageView) findViewById(R.id.iv_car_photo_validation);
        ivIdFrontValidation = (ImageView) findViewById(R.id.iv_id_front_validation);
        ivIdBackValidation = (ImageView) findViewById(R.id.iv_id_back_validation);
        ivDriverLicenceFrontValidation = (ImageView) findViewById(R.id.iv_driver_licence_front_validation);
        ivDriverLicenceBackValidation = (ImageView) findViewById(R.id.iv_driver_licence_back_validation);
        ivCarLicenceFrontValidation = (ImageView) findViewById(R.id.iv_car_licence_front_validation);
        ivCarLicenceBackValidation = (ImageView) findViewById(R.id.iv_car_licence_back_validation);
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
            ivUserPhotoValidation.setVisibility(View.GONE);
        }

        //Car Photo
        else if (requestCode == Const.REQUEST_GALLERY_CAR_PHOTO && resultCode == RESULT_OK) {
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

        //ID Front Photo
        else if (requestCode == Const.REQUEST_GALLERY_ID_FRONT && resultCode == RESULT_OK) {
            imageIdFrontPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_ID_FRONT);
            if (imageIdFrontPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageIdFrontPhotoCropped)).start(this, Const.REQUEST_CROP_ID_FRONT);
        } else if (requestCode == Const.REQUEST_CAMERA_ID_FRONT && resultCode == RESULT_OK) {
            imageIdFrontPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_ID_FRONT);
            if (imageIdFrontPhotoCropped != null && imageIdFrontPhoto != null)
                Crop.of(Uri.fromFile(imageIdFrontPhoto), Uri.fromFile(imageIdFrontPhotoCropped)).start(this, Const.REQUEST_CROP_ID_FRONT);
        } else if (requestCode == Const.REQUEST_CROP_ID_FRONT && resultCode == RESULT_OK) {
            BitmapUtils.resizeBitmap(imageIdFrontPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE, Const.IMAGE_SIZE);
            ivIdFrontValidation.setImageResource(R.drawable.success_icon);
        }

        //ID Back Photo
        else if (requestCode == Const.REQUEST_GALLERY_ID_BACK && resultCode == RESULT_OK) {
            imageIdBackPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_ID_BACK);
            if (imageIdBackPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageIdBackPhotoCropped)).start(this, Const.REQUEST_CROP_ID_BACK);
        } else if (requestCode == Const.REQUEST_CAMERA_ID_BACK && resultCode == RESULT_OK) {
            imageIdBackPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_ID_BACK);
            if (imageIdBackPhotoCropped != null && imageIdBackPhoto != null)
                Crop.of(Uri.fromFile(imageIdBackPhoto), Uri.fromFile(imageIdBackPhotoCropped)).start(this, Const.REQUEST_CROP_ID_BACK);
        } else if (requestCode == Const.REQUEST_CROP_ID_BACK && resultCode == RESULT_OK) {
            BitmapUtils.resizeBitmap(imageIdBackPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE, Const.IMAGE_SIZE);
            ivIdBackValidation.setImageResource(R.drawable.success_icon);
        }

        //Driver Licence Front Photo
        else if (requestCode == Const.REQUEST_GALLERY_DRIVER_LICENCE_FRONT && resultCode == RESULT_OK) {
            imageDriverLicenceFrontPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_DRIVER_LICENCE_FRONT);
            if (imageDriverLicenceFrontPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageDriverLicenceFrontPhotoCropped)).start(this, Const.REQUEST_CROP_DRIVER_LICENCE_FRONT);
        } else if (requestCode == Const.REQUEST_CAMERA_DRIVER_LICENCE_FRONT && resultCode == RESULT_OK) {
            imageDriverLicenceFrontPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_DRIVER_LICENCE_FRONT);
            if (imageDriverLicenceFrontPhotoCropped != null && imageDriverLicenceFrontPhoto != null)
                Crop.of(Uri.fromFile(imageDriverLicenceFrontPhoto), Uri.fromFile(imageDriverLicenceFrontPhotoCropped)).start(this, Const.REQUEST_CROP_DRIVER_LICENCE_FRONT);
        } else if (requestCode == Const.REQUEST_CROP_DRIVER_LICENCE_FRONT && resultCode == RESULT_OK) {
            BitmapUtils.resizeBitmap(imageDriverLicenceFrontPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE, Const.IMAGE_SIZE);
            ivDriverLicenceFrontValidation.setImageResource(R.drawable.success_icon);
        }

        //Driver Licence Back Photo
        else if (requestCode == Const.REQUEST_GALLERY_DRIVER_LICENCE_BACK && resultCode == RESULT_OK) {
            imageDriverLicenceBackPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_DRIVER_LICENCE_BACK);
            if (imageDriverLicenceBackPhotoCropped != null)
                Crop.of(data.getData(), Uri.fromFile(imageDriverLicenceBackPhotoCropped)).start(this, Const.REQUEST_CROP_DRIVER_LICENCE_BACK);
        } else if (requestCode == Const.REQUEST_CAMERA_DRIVER_LICENCE_BACK && resultCode == RESULT_OK) {
            imageDriverLicenceBackPhotoCropped = BitmapUtils.createImagePath(this, Const.FILE_NAME_CROPPED_DRIVER_LICENCE_BACK);
            if (imageDriverLicenceBackPhotoCropped != null && imageDriverLicenceBackPhoto != null)
                Crop.of(Uri.fromFile(imageDriverLicenceBackPhoto), Uri.fromFile(imageDriverLicenceBackPhotoCropped)).start(this, Const.REQUEST_CROP_DRIVER_LICENCE_BACK);
        } else if (requestCode == Const.REQUEST_CROP_DRIVER_LICENCE_BACK && resultCode == RESULT_OK) {
            BitmapUtils.resizeBitmap(imageDriverLicenceBackPhotoCropped.getAbsolutePath(), Const.IMAGE_SIZE, Const.IMAGE_SIZE);
            ivDriverLicenceBackValidation.setImageResource(R.drawable.success_icon);
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
            case R.id.btn_sign_up:
                driverSignup();
                break;
            case R.id.ib_user_photo:
                builder = new AlertDialog.Builder(this);
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
            case R.id.ib_id_front_gallery:
                requestGalleryImage(Const.REQUEST_GALLERY_ID_FRONT);
                break;
            case R.id.ib_id_front_camera:
                requestCameraImage(Const.REQUEST_CAMERA_ID_FRONT);
                break;
            case R.id.ib_id_back_gallery:
                requestGalleryImage(Const.REQUEST_GALLERY_ID_BACK);
                break;
            case R.id.ib_id_back_camera:
                requestCameraImage(Const.REQUEST_CAMERA_ID_BACK);
                break;
            case R.id.ib_driver_licence_front_gallery:
                requestGalleryImage(Const.REQUEST_GALLERY_DRIVER_LICENCE_FRONT);
                break;
            case R.id.ib_driver_licence_front_camera:
                requestCameraImage(Const.REQUEST_CAMERA_DRIVER_LICENCE_FRONT);
                break;
            case R.id.ib_driver_licence_back_gallery:
                requestGalleryImage(Const.REQUEST_GALLERY_DRIVER_LICENCE_BACK);
                break;
            case R.id.ib_driver_licence_back_camera:
                requestCameraImage(Const.REQUEST_CAMERA_DRIVER_LICENCE_BACK);
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
                case Const.REQUEST_CAMERA_CAR_PHOTO:
                    imageCarPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_CAR_PHOTO);
                    tempFile = imageCarPhoto;
                    break;
                case Const.REQUEST_CAMERA_ID_FRONT:
                    imageIdFrontPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_ID_FRONT);
                    tempFile = imageIdFrontPhoto;
                    break;
                case Const.REQUEST_CAMERA_ID_BACK:
                    imageIdBackPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_ID_BACK);
                    tempFile = imageIdBackPhoto;
                    break;
                case Const.REQUEST_CAMERA_DRIVER_LICENCE_FRONT:
                    imageDriverLicenceFrontPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_DRIVER_LICENCE_FRONT);
                    tempFile = imageDriverLicenceFrontPhoto;
                    break;
                case Const.REQUEST_CAMERA_DRIVER_LICENCE_BACK:
                    imageDriverLicenceBackPhoto = BitmapUtils.createImagePath(this, Const.FILE_NAME_DRIVER_LICENCE_BACK);
                    tempFile = imageDriverLicenceBackPhoto;
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
        Crop.pickImage(DriverSignupActivity.this, requestCode);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("imageUserPhoto", imageUserPhoto);
        outState.putSerializable("imageUserPhotoCropped", imageUserPhotoCropped);
        outState.putSerializable("imageCarPhoto", imageCarPhoto);
        outState.putSerializable("imageCarPhotoCropped", imageCarPhotoCropped);
        outState.putSerializable("imageIdFrontPhoto", imageIdFrontPhoto);
        outState.putSerializable("imageIdFrontPhotoCropped", imageIdFrontPhotoCropped);
        outState.putSerializable("imageIdBackPhoto", imageIdBackPhoto);
        outState.putSerializable("imageIdBackPhotoCropped", imageIdBackPhotoCropped);
        outState.putSerializable("imageDriverLicenceFrontPhoto", imageDriverLicenceFrontPhoto);
        outState.putSerializable("imageDriverLicenceFrontPhotoCropped", imageDriverLicenceFrontPhotoCropped);
        outState.putSerializable("imageDriverLicenceBackPhoto", imageDriverLicenceBackPhoto);
        outState.putSerializable("imageDriverLicenceBackPhotoCropped", imageDriverLicenceBackPhotoCropped);
        outState.putSerializable("imageCarLicenceFrontPhoto", imageCarLicenceFrontPhoto);
        outState.putSerializable("imageCarLicenceFrontPhotoCropped", imageCarLicenceFrontPhotoCropped);
        outState.putSerializable("imageCarLicenceBackPhoto", imageCarLicenceBackPhoto);
        outState.putSerializable("imageCarLicenceBackPhotoCropped", imageCarLicenceBackPhotoCropped);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        imageUserPhoto = (File) savedInstanceState.getSerializable("imageUserPhoto");
        imageUserPhotoCropped = (File) savedInstanceState.getSerializable("imageUserPhotoCropped");
        imageCarPhoto = (File) savedInstanceState.getSerializable("imageCarPhoto");
        imageCarPhotoCropped = (File) savedInstanceState.getSerializable("imageCarPhotoCropped");
        imageIdFrontPhoto = (File) savedInstanceState.getSerializable("imageIdFrontPhoto");
        imageIdFrontPhotoCropped = (File) savedInstanceState.getSerializable("imageIdFrontPhotoCropped");
        imageIdBackPhoto = (File) savedInstanceState.getSerializable("imageIdBackPhoto");
        imageIdBackPhotoCropped = (File) savedInstanceState.getSerializable("imageIdBackPhotoCropped");
        imageDriverLicenceFrontPhoto = (File) savedInstanceState.getSerializable("imageDriverLicenceFrontPhoto");
        imageDriverLicenceFrontPhotoCropped = (File) savedInstanceState.getSerializable("imageDriverLicenceFrontPhotoCropped");
        imageDriverLicenceBackPhoto = (File) savedInstanceState.getSerializable("imageDriverLicenceBackPhoto");
        imageDriverLicenceBackPhotoCropped = (File) savedInstanceState.getSerializable("imageDriverLicenceBackPhotoCropped");
        imageCarLicenceFrontPhoto = (File) savedInstanceState.getSerializable("imageCarLicenceFrontPhoto");
        imageCarLicenceFrontPhotoCropped = (File) savedInstanceState.getSerializable("imageCarLicenceFrontPhotoCropped");
        imageCarLicenceBackPhoto = (File) savedInstanceState.getSerializable("imageCarLicenceBackPhoto");
        imageCarLicenceBackPhotoCropped = (File) savedInstanceState.getSerializable("imageCarLicenceBackPhotoCropped");
    }

    private void driverSignup() {
        driverValidation();


    }

    private boolean driverValidation() {
        boolean valid = true;

        if(Utils.isEmpty(etFirstName)){
            etFirstName.setError(getString(R.string.first_name));
        }

        if (imageUserPhotoCropped == null) {
            ivUserPhotoValidation.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            ivUserPhotoValidation.setVisibility(View.GONE);
        }

        if (imageCarPhotoCropped == null) {
            ivCarPhotoValidation.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            ivCarPhotoValidation.setVisibility(View.GONE);
        }

        if (imageIdFrontPhotoCropped == null) {
            ivIdFrontValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivIdFrontValidation.setImageResource(0);
        }

        if (imageIdBackPhotoCropped == null) {
            ivIdBackValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivIdBackValidation.setImageResource(0);
        }

        if (imageDriverLicenceFrontPhotoCropped == null) {
            ivDriverLicenceFrontValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivDriverLicenceFrontValidation.setImageResource(0);
        }

        if (imageDriverLicenceBackPhotoCropped == null) {
            ivDriverLicenceBackValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivDriverLicenceBackValidation.setImageResource(0);
        }

        if (imageCarLicenceFrontPhotoCropped == null) {
            ivCarLicenceFrontValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivCarLicenceFrontValidation.setImageResource(0);
        }

        if (imageCarLicenceBackPhotoCropped == null) {
            ivCarLicenceBackValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivCarLicenceBackValidation.setImageResource(0);
        }


        return valid;
    }
}
