package com.privatecar.privatecar;

/**
 * Created by basim on 18/12/15.
 * A class contains constants for the application and for some of the utility classes.
 */

public class Const {
    public static final String LOG_TAG = "Private Car";
    public static final String SHARED_PREFERENCES_FILE_NAME = "private_car";
    public static final String APP_FILES_DIR = "/.private_car";
    public static final String PACKAGE_NAME = "com.privatecar.privatecar";
    public static final int IMAGE_SIZE = 500;

    //cached parameter names
    public static final String CACHE_CONFIGS = "configs_cache";
    public static final String CACHE_USER = "user_cache";


    //messages:----
    public static final String MESSAGES_BASE_URL = "http://ec2-54-201-70-234.us-west-2.compute.amazonaws.com/api-repo-1/public/messages/";
    public static final String MESSAGE_STARTUP_CONFIG = "startupconfig";
    public static final String MESSAGE_USER_CONFIG = "userconfig";
    public static final String MESSAGE_REGISTER_CUSTOMER = "regcustomer";
    public static final String MESSAGE_ACCESS_TOKEN = "accesstoken";

}
