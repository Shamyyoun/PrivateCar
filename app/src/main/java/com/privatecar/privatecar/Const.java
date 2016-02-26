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

    //cached parameter names:---------------
    public static final String CACHE_CONFIGS = "configs_cache";
    public static final String CACHE_USER = "user_cache";

    //messages:----
    public static final String MESSAGES_BASE_URL = "http://ec2-54-201-70-234.us-west-2.compute.amazonaws.com/api-repo-1/public/messages/";
    public static final String MESSAGE_STARTUP_CONFIG = "startupconfig";
    public static final String MESSAGE_USER_CONFIG = "userconfig";
    public static final String MESSAGE_REGISTER_CUSTOMER = "regcustomer";
    public static final String MESSAGE_ACCESS_TOKEN = "accesstoken";
    public static final String MESSAGE_DRIVER_ACCOUNT_DETAILS = "driveraccountdetails";

    //parameters:----
    public static final String MSG_PARAM_CLIENT_ID = "client_id";
    public static final String MSG_PARAM_CLIENT_SECRET = "client_secret";
    public static final String MSG_PARAM_GRANT_TYPE = "grant_type";
    public static final String MSG_PARAM_USERNAME = "username";
    public static final String MSG_PARAM_PASSWORD = "password";
    public static final String MSG_PARAM_OS = "os";
    public static final String MSG_PARAM_GCM_ACCESS_TOKEN = "gcm_access_token";
    public static final String MSG_PARAM_ACCESS_TOKEN = "access_token";
}
