package com.privatecar.privatecar.utils;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.entities.Ad;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.Message;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.ConfigResponse;

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
}
