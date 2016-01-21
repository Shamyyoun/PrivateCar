package com.privatecar.privatecar.utils;

import android.content.Context;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.User;

/**
 * Created by basim on 22/1/16.
 * A class, with utility methods useful only for the current project "private car"
 */
public class AppUtils {

    /**
     * Cache app configs (retrieved by startupconfig message)
     *
     * @param ctx     the application context
     * @param configs the array of configs to cache
     */
    public static void cacheConfigs(Context ctx, Config[] configs) {
        SavePrefs<Config[]> savePrefs = new SavePrefs<>(ctx, Config[].class);
        savePrefs.save(configs, Const.CACHE_CONFIGS);
    }

    /**
     * retrieve cached app configs
     *
     * @param ctx the application config
     * @return the array of configs if cached otherwise null.
     */
    public static Config[] getCachedConfigs(Context ctx) {
        SavePrefs<Config[]> savePrefs = new SavePrefs<>(ctx, Config[].class);
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
        Config[] configs = getCachedConfigs(ctx);

        if (configs != null) {
            for (Config config : configs) {
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

    public static boolean isUserLoggedIn(Context ctx) {
        return getCachedUser(ctx) != null;
    }


}
