package com.privateegy.privatecar.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.models.entities.Ad;
import com.privateegy.privatecar.models.entities.Config;
import com.privateegy.privatecar.models.entities.CustomerTripRequest;
import com.privateegy.privatecar.models.entities.Message;
import com.privateegy.privatecar.models.entities.TripInfo;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.responses.ConfigResponse;

import java.util.Arrays;
import java.util.List;

/**
 * Created by basim on 22/1/16.
 * A class, with utility methods useful only for the current project "private car"
 */
public class AppUtils {

    /**
     * Cache app configs (retrieved by startupconfig message)
     *
     * @param ctx            the application context
     * @param configResponse config response returned from the server
     */
    public static void cacheConfigs(Context ctx, ConfigResponse configResponse) {
        SavePrefs<ConfigResponse> savePrefs = new SavePrefs<>(ctx, ConfigResponse.class);
        savePrefs.save(configResponse, Const.CACHE_CONFIGS);
    }

    /**
     * retrieve cached app configs
     *
     * @param ctx the application config
     * @return the config response if cached otherwise null.
     */
    public static ConfigResponse getCachedConfigs(Context ctx) {
        SavePrefs<ConfigResponse> savePrefs = new SavePrefs<>(ctx, ConfigResponse.class);
        return savePrefs.load(Const.CACHE_CONFIGS);
    }

    /**
     * find the value of a specific config.
     *
     * @param ctx       the application context
     * @param configKey the key of the config to get its value
     * @return the value if config is cached and found otherwise null.
     */
    public static String getConfigValue(Context ctx, String configKey) {
        ConfigResponse configResponse = getCachedConfigs(ctx);

        if (configResponse != null) {
            for (Config config : configResponse.getConfigs()) {
                if (config.getKey().equals(configKey))
                    return config.getValue();
            }
        }

        return null;
    }

    public static void cacheUser(Context ctx, User user) {
        SavePrefs<User> savePrefs = new SavePrefs<>(ctx, User.class);
        savePrefs.save(user, Const.CACHE_USER);
    }

    public static User getCachedUser(Context ctx) {
        SavePrefs<User> savePrefs = new SavePrefs<>(ctx, User.class);
        return savePrefs.load(Const.CACHE_USER);
    }

    public static void cacheAds(Context ctx, List<Ad> ads) {
        SavePrefs<List<Ad>> savePrefs = new SavePrefs<>(ctx, Ad[].class);
        savePrefs.save(ads, Const.CACHE_ADS);
    }

    public static List<Ad> getCachedAds(Context ctx) {
        SavePrefs<Ad[]> savePrefs = new SavePrefs<>(ctx, Ad[].class);
        return Arrays.asList(savePrefs.load(Const.CACHE_ADS));
    }

    public static TripInfo getLastCachedTripInfo(Context ctx) {
        SavePrefs<TripInfo> savePrefs = new SavePrefs<>(ctx, TripInfo.class);
        return savePrefs.load(Const.CACHE_LAST_TRIP_INFO);
    }

    public static CustomerTripRequest getLastCachedTripRequest(Context ctx) {
        SavePrefs<CustomerTripRequest> savePrefs = new SavePrefs<>(ctx, CustomerTripRequest.class);
        return savePrefs.load(Const.CACHE_LAST_TRIP_REQUEST);
    }

    public static boolean isUserLoggedIn(Context ctx) {
        return getCachedUser(ctx) != null;
    }


    /**
     * New token will be generated if there is less than 6 days for it to expire
     *
     * @param expiryTimestamp
     * @return true if (expiryTimestamp - currentTimestamp) < milliSecondsIn6Days
     */
    public static boolean isTokenExpired(long expiryTimestamp) {
        long currentTimestamp = System.currentTimeMillis();
        long milliSecondsIn6Days = 518400000;

        return (expiryTimestamp - currentTimestamp) < milliSecondsIn6Days;
    }

    /**
     * Cache message center messages
     *
     * @param ctx
     * @param messages
     */
    public static void cacheMessages(Context ctx, List<Message> messages) {
        SavePrefs<List<Message>> savePrefs = new SavePrefs<>(ctx, Message[].class);
        savePrefs.save(messages, Const.CACHE_MESSAGES);
    }

    /**
     * Get cached messages to load it into message center
     *
     * @param ctx
     * @return List of messages or null if no messages cached
     */
    public static List<Message> getCachedMessages(Context ctx) {
        SavePrefs<Message[]> savePrefs = new SavePrefs<>(ctx, Message[].class);
        Message[] messages = savePrefs.load(Const.CACHE_MESSAGES);
        if (messages != null)
            return Arrays.asList(messages);

        return null;
    }

    /**
     * method, used to clear all cache to sign out the user
     *
     * @param context
     */
    public static void clearCache(Context context) {
        // clear user prefs
        SavePrefs<User> userPrefs = new SavePrefs<>(context, User.class);
        userPrefs.clear();

        // clear ads prefs
        SavePrefs<List<Ad>> adsPrefs = new SavePrefs<>(context, Ad[].class);
        adsPrefs.clear();

        // clear messages prefs
        SavePrefs<List<Message>> messagePrefs = new SavePrefs<>(context, Message[].class);
        messagePrefs.clear();

        // clear locale
        Utils.clearCachedKey(context, Const.CACHE_LOCALE);
    }

    /**
     * method, used to show call customer service dialog
     *
     * @param context
     */
    public static void showCallCustomerServiceDialog(Context context) {
        final String customerServiceNumber = getConfigValue(context, Config.KEY_CUSTOMER_SERVICE_NUMBER);
        DialogUtils.showCallDialog(context, customerServiceNumber);
    }

    /**
     * method, used to show call driver service dialog
     *
     * @param context
     */
    public static void showCallDriverServiceDialog(Context context) {
        final String customerServiceNumber = getConfigValue(context, Config.KEY_DRIVER_SERVICE_NUMBER);
        DialogUtils.showCallDialog(context, customerServiceNumber);
    }



    /**
     * method, used to calculate the trip estimate fare based on passed params (no waiting time included)
     * @param context
     * @param distance
     * @param openFare
     * @param kmFare
     * @return
     */
    public static float calculateEstimateFare(Context context, float distance, float openFare, float kmFare) {
        // get min fare from configs
        float minFare = 0;
        String minFareStr = getConfigValue(context, Config.KEY_MIN_TRIP_FARE);
        try {
            minFare = Float.parseFloat(minFareStr);
        } catch (Exception e) {
            Log.e("ERROR", "Can't convert min fare (" + minFareStr + ") to float");
        }

        // calculate the fare
        float fares = openFare + (distance * kmFare);
        fares = fares < minFare ? minFare : fares;

        return fares;
    }


    /**
     * Convert location string to LatLng
     * @param location string like "34.3434,22.34334"
     * @return LatLng object
     */
    public static LatLng getLatLng(String location) {
        String[] latLngArr = location.split(",");
        double latitude = Double.parseDouble(latLngArr[0]);
        double longitude = Double.parseDouble(latLngArr[1]);
        return new LatLng(latitude, longitude);
    }
}
