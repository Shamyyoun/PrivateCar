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
    public static final int REQUEST_GPS_SETTINGS = 998;
    public static final int REQUEST_COARSE_LOCATION_PERMISSION = 999;
    public static final int REQUEST_FINE_LOCATION_PERMISSION = 1000;
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
    public static final int REQUEST_MESSAGE_DETAILS = 1025;
    public static final int REQUEST_GOOGLE_PLUS_SIGN_IN = 1026;
    public static final int REQUEST_ADD_DETAILS = 1027;

    //app level constants:--------------
    public static final String LOG_TAG = "Private Car";
    public static final String SHARED_PREFERENCES_FILE_NAME = "private_car";
    public static final String APP_FILES_DIR = "/.private_car";
    public static final String PACKAGE_NAME = "com.privatecar.privatecar";
    public static final int IMAGE_SIZE_USER = 400;
    public static final int IMAGE_SIZE = 1024;
    public static final int EGYPT_INDEX = 61; //Egypt index in the country spinner
    public static final int MIN_PASSWORD_LENGTH = 3;
    public static final int LOCATION_UPDATE_IN_MS = 2000; //location is updated in how many milliseconds
    public static final int HEATMAP_RADIUS = 50; // in KM

    //File names
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
    public static final String CACHE_ADS = "ads_cache";
    public static final String CACHE_MESSAGES = "messages_cache";
    public static final String CACHE_LOCALE = "app_locale_cache";

    //messages (common):----
    public static final String IMAGES_BASE_URL = "http://ec2-54-201-70-234.us-west-2.compute.amazonaws.com/api-repo-1/";
    public static final String MESSAGES_BASE_URL = "http://ec2-54-201-70-234.us-west-2.compute.amazonaws.com/api-repo-1/public/messages/";
    public static final String MESSAGE_STARTUP_CONFIG = "startupconfig";
    public static final String MESSAGE_USER_CONFIG = "userconfig";
    public static final String MESSAGE_ACCESS_TOKEN = "accesstoken";
    public static final String MESSAGE_GET_OPTIONS = "getoptions";
    public static final String MESSAGE_CHANGE_PASSWORD = "changepassword";
    public static final String MESSAGE_FORGET_PASSWORD = "forgetpassword";

    //messages (customer):----
    public static final String MESSAGE_REGISTER_CUSTOMER = "regcustomer";
    public static final String MESSAGE_CUSTOMER_ACCOUNT_DETAILS = "customeraccountdetails";
    public static final String MESSAGE_NEAR_DRIVERS = "neardrivers";
    public static final String MESSAGE_CUSTOMER_VERIFY_USER = "verifyuser";
    public static final String MESSAGE_CUSTOMER_TRIPS = "customertrips";
    public static final String MESSAGE_CUSTOMER_FARES = "getfares";
    public static final String MESSAGE_CUSTOMER_ACTIVATE_PROMO_CODE = "activatepromocode";


    //messages (driver):----
    public static final String MESSAGE_DRIVER_ACCOUNT_DETAILS = "driveraccountdetails";
    public static final String MESSAGE_DRIVER_SIGNUP = "regdriver";
    public static final String MESSAGE_DRIVER_LAST_TRIP = "lasttrip";
    public static final String MESSAGE_DRIVER_GET_CUSTOMERS_STATS = "getcustomersstats";
    public static final String MESSAGE_DRIVER_UPDATE_LOCATION = "updatelocation";
    public static final String MESSAGE_DRIVER_GET_STATEMENT = "getstatment";
    public static final String MESSAGE_DRIVER_DECLINE_TRIP = "declinetrip";
    public static final String MESSAGE_DRIVER_ACCEPT_TRIP = "accepttrip";
    public static final String MESSAGE_DRIVER_START_TRIP = "starttrip";
    public static final String MESSAGE_DRIVER_CANCEL_TRIP = "drivercanceltrip";
    public static final String MESSAGE_DRIVER_GET_ADS = "getads";
    public static final String MESSAGE_DRIVER_GET_INBOX = "getinbox";
    public static final String MESSAGE_DRIVER_SEND_MESSAGE = "sendmessage";
    public static final String MESSAGE_DRIVER_MESSAGES_MARK_AS_READ = "inboxmarkasread";
    public static final String MESSAGE_DRIVER_MESSAGES_MARK_AS_UNREAD = "inboxmarkasunread";
    public static final String MESSAGE_DRIVER_MESSAGES_DELETE = "inboxdelete";
    public static final String MESSAGE_DRIVER_CHANGE_CAR_TYPE = "changecartype";
    public static final String MESSAGE_DRIVER_GET_DOCUMENTS = "getdocuments";
    public static final String MESSAGE_DRIVER_ADD_CAR = "addcar";
    public static final String MESSAGE_DRIVER_CHANGE_PHOTO = "changephoto";


    //parameters:----
    public static final String MSG_PARAM_CLIENT_ID = "client_id";
    public static final String MSG_PARAM_CLIENT_SECRET = "client_secret";
    public static final String MSG_PARAM_GRANT_TYPE = "grant_type";
    public static final String MSG_PARAM_USERNAME = "username";
    public static final String MSG_PARAM_PASSWORD = "password";
    public static final String MSG_PARAM_OS = "os";
    public static final String MSG_PARAM_GCM_ACCESS_TOKEN = "gcm_access_token";
    public static final String MSG_PARAM_ACCESS_TOKEN = "access_token";
    public static final String MSG_PARAM_FROM = "form";
    public static final String MSG_PARAM_TO = "to";
    public static final String MSG_PARAM_TYPE = "type";
    public static final String MSG_PARAM_DRIVER_ID = "driverId";
    public static final String MSG_PARAM_TRIP_ID = "tripId";
    public static final String MSG_PARAM_CAR_ID = "carId";
    public static final String MSG_PARAM_REASON_ID = "reasonId";
    public static final String MSG_PARAM_COMMENT = "comment";
    public static final String MSG_PARAM_CAR_TYPE = "carType";
    public static final String MSG_PARAM_OLD_PASSWORD = "oldpassword";
    public static final String MSG_PARAM_NEW_PASSWORD = "newpassword";
    public static final String MSG_PARAM_EMAIL = "email";
    public static final String MSG_PARAM_ID = "id";
    public static final String MSG_PARAM_TOKEN = "token";
    public static final String MSG_PARAM_PROVIDER = "provider";
    public static final String MSG_PARAM_CODE = "code";
    public static final String MSG_PARAM_CUSTOMER_ID = "CustomerId";
    public static final String MSG_PARAM_CLASS = "class";
    public static final String MSG_PARAM_PICKUP_TIME = "pickup_time";
    public static final String MSG_PARAM_PROMO_CODE = "PromoCode";

    //Intent & bundle keys
    public static final String KEY_TRIP_REQUEST = "trip_request";
    public static final String KEY_STATEMENT_GROUPS = "statements_groups";
    public static final String KEY_TRIP_ID = "trip_id";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_PROVIDER = "provider";
    public static final String KEY_NOW = "now";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PICKUP_PLACE = "pickup_place";
    public static final String KEY_DETAILS = "details";
}
