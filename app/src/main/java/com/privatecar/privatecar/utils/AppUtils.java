package com.privatecar.privatecar.utils;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.config.ConfigResponse;
import com.privatecar.privatecar.models.responses.config.Content;

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
            for (Content configContent : configResponse.getContent()) {
                if (configContent.getKey().equals(configKey))
                    return configContent.getValue();
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

    public static boolean isUserLoggedIn(Context ctx) {
        return getCachedUser(ctx) != null;
    }


}
