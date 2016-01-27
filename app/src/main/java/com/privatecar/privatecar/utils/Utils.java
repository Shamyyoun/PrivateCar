package com.privatecar.privatecar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.privatecar.privatecar.Const;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by basim on 18/12/15.
 * A class, with general purpose utility methods (useful for many projects).
 */
public class Utils {

    public static final String KEY_APP_VERSION_CODE = "app_version_code_key";

    /**
     * get the hash key for usage with facebook sdk
     */
    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(Const.PACKAGE_NAME, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(Const.LOG_TAG, "Hash key:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
        }
    }


    /**
     * Validate email address.
     *
     * @param email the email to validate
     * @return true if valid email or false.
     */
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email)) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /**
     * Checks the given number as a valid Egyptian mobile number.
     *
     * @param number the number to check
     * @return true if valid Egyptian mobile number.
     */
    public static boolean isValidEgyptianMobileNumber(String number) {
        if (number.length() < 11) {
            return false;
        } else if (!number.startsWith("01")) {
            return false;
        } else {
            char operatorDifferentiator = number.charAt(2);

            if (operatorDifferentiator != '0' && operatorDifferentiator != '1' && operatorDifferentiator != '2') {
                return false;
            } else {
                try {
                    long l = Long.parseLong(number);
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Get a shared preferences file named Const.SHARED_PREFERENCES_FILE_NAME, keys added to it must be unique
     *
     * @param ctx
     * @return the shared preferences
     */
    public static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(Const.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void cacheBoolean(Context ctx, String k, Boolean v) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        prefs.edit().putBoolean(k, v).apply();
    }

    public static Boolean getCachedBoolean(Context ctx, String k, Boolean defaultValue) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getBoolean(k, defaultValue);
    }

    public static void cacheString(Context ctx, String k, String v) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        prefs.edit().putString(k, v).apply();
    }

    public static String getCachedString(Context ctx, String k, String defaultValue) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getString(k, defaultValue);
    }

    public static void cacheInt(Context ctx, String k, int v) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        prefs.edit().putInt(k, v).apply();
    }

    public static int getCachedInt(Context ctx, String k, int defaultValue) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getInt(k, defaultValue);
    }

    public static void clearCachedKey(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).apply();
    }

    /**
     * Checks the device has a connection (not necessarily connected to the internet)
     *
     * @param ctx
     * @return
     */
    public static boolean hasConnection(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Gets a formatted % percent from numerator/denominator values.
     *
     * @param numerator
     * @param denominator
     * @param locale      the locale of the returned string.
     * @return the formatted percent.
     */
    public static String getPercent(int numerator, int denominator, Locale locale) {
        //http://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html
        NumberFormat nf = NumberFormat.getNumberInstance(locale);  //Locale.US, .....
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("###.#");
        if (denominator == 0) {
            return df.format(0) + "%";
        }
        float percent = (numerator / (float) denominator) * 100;
        return df.format(percent) + "%";
    }


    /**
     * hide keyboard in edit text field
     */
    public void hideKeyboard(Activity activity, EditText et) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * show key board in edit text field
     */
    public void showKeyboard(Activity activity, EditText et) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInputFromInputMethod(et.getWindowToken(), 0);
    }


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }


    public static void showToast(Context context, int textID) {
        Toast.makeText(context, textID, Toast.LENGTH_LONG).show();
    }

    public static void LogE(String msg) {
        Log.e(Const.LOG_TAG, msg);
    }


    /**
     * Executes the given AsyncTask Efficiently.
     *
     * @param task the task to execute.
     */
    public static void executeAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    /**
     * Remove '-' and other characters from mobile numbers and replace any + with 00
     *
     * @param oldNumber
     * @return the enhanced number
     */
    public static String enhanceMobileNumber(String oldNumber) {
        return oldNumber.replaceAll("[()\\s-]", "").replace("+", "00");
    }

    public static Boolean isEmpty(EditText et) {
        return TextUtils.isEmpty(et.getText().toString().trim());
    }

    /**
     * converts the given timestamp to  readable date.
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToString(long timeStamp, Locale locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
        return dateFormat.format(new Date(timeStamp));
    }


    /**
     * Gets the app version code.
     *
     * @param context
     * @return the version code.
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static void cacheAppVersionCode(Context context) {
        cacheInt(context, KEY_APP_VERSION_CODE, getAppVersionCode(context));
    }

    public static int getCachedAppVersionCode(Context context) {
        return getCachedInt(context, KEY_APP_VERSION_CODE, Integer.MIN_VALUE);
    }

    /**
     * Gets the current app locale language like: ar, en, ....
     *
     * @return the current app locale language like: ar, en, ....
     */
    public static String getAppLanguage() {
        return Locale.getDefault().getLanguage().toLowerCase().substring(0, 2);
    }

    /**
     * Set the app language
     *
     * @param ctx  the application context
     * @param lang the language as ar, en, .....
     */
    public static void changeAppLocale(Context ctx, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
    }

}
