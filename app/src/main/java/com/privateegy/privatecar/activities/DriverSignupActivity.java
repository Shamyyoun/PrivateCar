package com.privateegy.privatecar.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.adapters.CountryAdapter;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.utils.BitmapUtils;
import com.privateegy.privatecar.utils.CountriesUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;
import com.soundcloud.android.crop.Crop;

import java.io.File;

public class DriverSignupActivity extends BasicBackActivity implements RequestListener<GeneralResponse> {

    private EditText etFirstName, etLastName, etMobile, etEmail, etPassword;
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
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup);

        etFirstName = (EditText) findViewById(R.id.et_first_name);
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    etFirstName.setError(getString(R.string.required));
                } else {
                    etFirstName.setError(null);
                }
            }
        });


        etLastName = (EditText) findViewById(R.id.et_last_name);
        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    etLastName.setError(getString(R.string.required));
                } else {
                    etLastName.setError(null);
                }
            }
        });

        etEmail = (EditText) findViewById(R.id.et_email);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Utils.isValidEmail(s.toString())) {
                    etEmail.setError(getString(R.string.not_valid_email));
                } else {
                    etEmail.setError(null);
                }
            }
        });

        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < Const.MIN_PASSWORD_LENGTH) {
                    etPassword.setError(getString(R.string.short_password));
                } else {
                    etPassword.setError(null);
                }
            }
        });

        etMobile = (EditText) findViewById(R.id.et_mobile);
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (spinner.getSelectedItemPosition() == Const.EGYPT_INDEX) {
                    if (!Utils.isValidEgyptianMobileNumber(s.toString()) && !Utils.isValidEgyptianMobileNumber("0" + s.toString())) {
                        etMobile.setError(getString(R.string.not_valid_mobile));
                    } else {
                        etMobile.setError(null);
                    }
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner_countries);
        CountryAdapter adapter = new CountryAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(Const.EGYPT_INDEX); //set egypt the default

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

        if (imageUserPhotoCropped != null) {
            Glide.with(this).load(imageUserPhotoCropped).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ibUserPhoto);
        }

        if (imageCarPhotoCropped != null) {
            Glide.with(this).load(imageCarPhotoCropped).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ibCarPhoto);
        }

        if (imageIdFrontPhotoCropped != null) {
            ivIdFrontValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageIdBackPhotoCropped != null) {
            ivIdBackValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageDriverLicenceFrontPhotoCropped != null) {
            ivDriverLicenceFrontValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageDriverLicenceBackPhotoCropped != null) {
            ivDriverLicenceBackValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageCarLicenceFrontPhotoCropped != null) {
            ivCarLicenceFrontValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageCarLicenceBackPhotoCropped != null) {
            ivCarLicenceBackValidation.setImageResource(R.drawable.success_icon);
        }

    }

    private boolean driverValidation() {
        boolean valid = true;

        if (Utils.isEmpty(etFirstName)) {
            etFirstName.setError(getString(R.string.required));
            valid = false;
        } else {
            etFirstName.setError(null);
        }

        if (Utils.isEmpty(etLastName)) {
            etLastName.setError(getString(R.string.required));
            valid = false;
        } else {
            etLastName.setError(null);
        }

        if (!Utils.isValidEmail(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.not_valid_email));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (Utils.getText(etPassword).length() == 0) {
            etPassword.setError(getString(R.string.required));
            valid = false;
        } else if (Utils.getText(etPassword).length() < Const.MIN_PASSWORD_LENGTH) {
            etPassword.setError(getString(R.string.short_password));
            valid = false;
        } else {
            etPassword.setError(null);
        }

        if (spinner.getSelectedItemPosition() == Const.EGYPT_INDEX) {
            if (!Utils.isValidEgyptianMobileNumber(Utils.getText(etMobile)) && !Utils.isValidEgyptianMobileNumber("0" + Utils.getText(etMobile))) {
                etMobile.setError(getString(R.string.not_valid_mobile));
                valid = false;
            } else {
                etMobile.setError(null);
            }
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
            ivIdFrontValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageIdBackPhotoCropped == null) {
            ivIdBackValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivIdBackValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageDriverLicenceFrontPhotoCropped == null) {
            ivDriverLicenceFrontValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivDriverLicenceFrontValidation.setImageResource(R.drawable.success_icon);
        }

        if (imageDriverLicenceBackPhotoCropped == null) {
            ivDriverLicenceBackValidation.setImageResource(R.drawable.fail_icon);
            valid = false;
        } else {
            ivDriverLicenceBackValidation.setImageResource(R.drawable.success_icon);
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

    private void driverSignup() {

        if (!driverValidation()) //if the validation failed
            return;

        String code = new CountriesUtils().getCountryCodes()[spinner.getSelectedItemPosition()];
        String mobile = Utils.getText(etMobile);
        if (spinner.getSelectedItemPosition() == Const.EGYPT_INDEX && mobile.charAt(0) == '0') //if the egyptian number starts with 0
            code = "+2";

        dialog = DialogUtils.showProgressDialog(this, R.string.registering, false);
        Utils.hideKeyboard(etMobile);
        DriverRequests.driverSignup(this, this, Utils.getText(etFirstName), Utils.getText(etLastName), Utils.getText(etEmail), Utils.getText(etPassword), code + mobile, imageUserPhotoCropped, imageCarPhotoCropped, imageIdFrontPhotoCropped, imageIdBackPhotoCropped, imageDriverLicenceFrontPhotoCropped, imageDriverLicenceBackPhotoCropped, imageCarLicenceFrontPhotoCropped, imageCarLicenceBackPhotoCropped);

    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        dialog.dismiss();

        if (response.isSuccess()) {
            startActivity(new Intent(this, DriverSignupConfirmationActivity.class));
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

