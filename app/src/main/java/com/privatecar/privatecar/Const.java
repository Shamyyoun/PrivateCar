package com.privatecar.privatecar;

/**
 * Created by basim on 18/12/15.
 * A class contains constants for the application and for some of the utility classes.
 */

public class Const {
    //Authentication:------------------
    public static final String CLIENT_ID = "3d259ddd3ed8ff3843839b";
    public static final String CLIENT_SECRET = "4c7f6f8fa93d59c45502c0ae8c4a95b";

    //activity requests:----------------
    public static final int REQ_GPS_SETTINGS = 1;

    //app level constants:--------------
    public static final String LOG_TAG = "Private Car";
    public static final String SHARED_PREFERENCES_FILE_NAME = "private_car";
    public static final String APP_FILES_DIR = "/.private_car";
    public static final String PACKAGE_NAME = "com.privatecar.privatecar";
    public static final int IMAGE_SIZE_USER = 400;
    public static final int IMAGE_SIZE = 1024;
    public static final int EGYPT_INDEX = 61; //Egypt index in the country spinner
    public static final int MIN_PASSWORD_LENGTH = 3;


    public static final int REQUEST_CAMERA_USER_PHOTO = 1001;
    public static final int REQUEST_CAMERA_CAR_PHOTO = 1002;
    public static final int REQUEST_CAMERA_ID_FRONT = 1003;
    public static final int REQUEST_CAMERA_ID_BACK = 1004;
    public static final int REQUEST_CAMERA_DRIVER_LICENCE_FRONT = 1005;
    public static final int REQUEST_CAMERA_DRIVER_LICENCE_BACK = 1006;
    public static final int REQUEST_CAMERA_CAR_LICENCE_FRONT = 1007;
    public static final int REQUEST_CAMERA_CAR_LICENCE_BACK = 1008;
    public static final int REQUEST_GALLERY_USER_PHOTO = 1009;
    public static final int REQUEST_GALLERY_CAR_PHOTO = 1010;
    public static final int REQUEST_GALLERY_ID_FRONT = 1011;
    public static final int REQUEST_GALLERY_ID_BACK = 1012;
    public static final int REQUEST_GALLERY_DRIVER_LICENCE_FRONT = 1013;
    public static final int REQUEST_GALLERY_DRIVER_LICENCE_BACK = 1014;
    public static final int REQUEST_GALLERY_CAR_LICENCE_FRONT = 1015;
    public static final int REQUEST_GALLERY_CAR_LICENCE_BACK = 1016;
    public static final int REQUEST_CROP_USER_PHOTO = 1017;
    public static final int REQUEST_CROP_CAR_PHOTO = 1018;
    public static final int REQUEST_CROP_ID_FRONT = 1019;
    public static final int REQUEST_CROP_ID_BACK = 1020;
    public static final int REQUEST_CROP_DRIVER_LICENCE_FRONT = 1021;
    public static final int REQUEST_CROP_DRIVER_LICENCE_BACK = 1022;
    public static final int REQUEST_CROP_CAR_LICENCE_FRONT = 1023;
    public static final int REQUEST_CROP_CAR_LICENCE_BACK = 1024;


    public static final String FILE_NAME_USER_PHOTO = "user_photo";
    public static final String FILE_NAME_CROPPED_USER_PHOTO = "user_photo_cropped";
    public static final String FILE_NAME_CAR_PHOTO = "car_photo";
    public static final String FILE_NAME_CROPPED_CAR_PHOTO = "car_photo_cropped";
    public static final String FILE_NAME_ID_FRONT = "id_front_photo";
    public static final String FILE_NAME_CROPPED_ID_FRONT = "id_front_photo_cropped";
    public static final String FILE_NAME_ID_BACK = "id_back_photo";
    public static final String FILE_NAME_CROPPED_ID_BACK = "id_back_photo_cropped";
    public static final String FILE_NAME_DRIVER_LICENCE_FRONT = "driver_licence_front_photo";
    public static final String FILE_NAME_CROPPED_DRIVER_LICENCE_FRONT = "driver_licence_front_photo_cropped";
    public static final String FILE_NAME_DRIVER_LICENCE_BACK = "driver_licence_back_photo";
    public static final String FILE_NAME_CROPPED_DRIVER_LICENCE_BACK = "driver_licence_back_photo_cropped";
    public static final String FILE_NAME_CAR_LICENCE_FRONT = "car_licence_front_photo";
    public static final String FILE_NAME_CROPPED_CAR_LICENCE_FRONT = "car_licence_front_photo_cropped";
    public static final String FILE_NAME_CAR_LICENCE_BACK = "car_licence_back_photo";
    public static final String FILE_NAME_CROPPED_CAR_LICENCE_BACK = "car_licence_back_photo_cropped";


    //cached parameter names:---------------
    public static final String CACHE_CONFIGS = "configs_cache";
    public static final String CACHE_USER = "user_cache";

    //messages (common):----
    public static final String IMAGES_BASE_URL = "http://ec2-54-201-70-234.us-west-2.compute.amazonaws.com/api-repo-1/";
    public static final String MESSAGES_BASE_URL = "http://ec2-54-201-70-234.us-west-2.compute.amazonaws.com/api-repo-1/public/messages/";
    public static final String MESSAGE_STARTUP_CONFIG = "startupconfig";
    public static final String MESSAGE_USER_CONFIG = "userconfig";
    public static final String MESSAGE_ACCESS_TOKEN = "accesstoken";

    //messages (customer):----
    public static final String MESSAGE_REGISTER_CUSTOMER = "regcustomer";

    //messages (driver):----
    public static final String MESSAGE_DRIVER_ACCOUNT_DETAILS = "driveraccountdetails";
    public static final String MESSAGE_DRIVER_SIGNUP = "regdriver";
    public static final String MESSAGE_DRIVER_LAST_TRIP = "lasttrip";


    //parameters:----
    public static final String MSG_PARAM_CLIENT_ID = "client_id";
    public static final String MSG_PARAM_CLIENT_SECRET = "client_secret";
    public static final String MSG_PARAM_GRANT_TYPE = "grant_type";
    public static final String MSG_PARAM_USERNAME = "username";
    public static final String MSG_PARAM_PASSWORD = "password";
    public static final String MSG_PARAM_OS = "os";
    public static final String MSG_PARAM_GCM_ACCESS_TOKEN = "gcm_access_token";
    public static final String MSG_PARAM_ACCESS_TOKEN = "access_token";

    //Intent & bundle keys
    public static final String KEY_TRIP_REQUEST = "trip_request";
}
