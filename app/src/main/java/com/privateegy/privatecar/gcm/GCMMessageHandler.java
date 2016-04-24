package com.privateegy.privatecar.gcm;

/**
 * Created by Basim Alamuddin on 13/12/2015.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.activities.CustomerAccountPaymentActivity;
import com.privateegy.privatecar.activities.CustomerCashPaymentActivity;
import com.privateegy.privatecar.activities.CustomerRideActivity;
import com.privateegy.privatecar.activities.CustomerVerifyTripActivity;
import com.privateegy.privatecar.activities.DriverTripInfoActivity;
import com.privateegy.privatecar.activities.DriverTripRequestActivity;
import com.privateegy.privatecar.models.entities.TripInfo;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.enums.UserType;
import com.privateegy.privatecar.models.payload.AcceptTripPayload;
import com.privateegy.privatecar.models.payload.EndCreditTripPayload;
import com.privateegy.privatecar.models.payload.TripRequestPayload;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.SavePrefs;
import com.privateegy.privatecar.utils.Utils;

import org.json.JSONObject;

public class GCMMessageHandler extends GcmListenerService {
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        try {
            // create gson object and get json string
            final Gson gson = new Gson();
            final String json = data.getString("message");

            Log.e(Const.LOG_TAG, "GCM Message: " + json);

            // get the key
            final JSONObject object = new JSONObject(json);
            final String key = object.optString("key");

            // run the following code in the main thread to prevent exceptions
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    // check logged in user type
                    User user = AppUtils.getCachedUser(context);
                    if (user != null && user.getType() != null && user.getType() == UserType.DRIVER) { //--------driver---------
                        // this is a driver user
                        // check the key
                        if (key.equals(Const.GCM_KEY_TRIP_REQUEST)) {
                            // this is a trip request
                            // parse the json string
                            TripRequestPayload requestPayload = gson.fromJson(json, TripRequestPayload.class);

                            // open trip request activity
                            Intent intent = new Intent(context, DriverTripRequestActivity.class);
                            intent.putExtra(Const.KEY_TRIP_REQUEST, requestPayload.getTripRequest());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else if (key.equals(Const.GCM_KEY_CUSTOMER_CANCEL)) {
                            // cancel the trip
                            // show msg and finish the driver ride activity if available
                            Utils.showLongToast(context, R.string.driver_cancel_trip_message);
                            if (DriverTripInfoActivity.currentInstance != null) {
                                DriverTripInfoActivity.currentInstance.finish();
                            }
                        } else if (key.equals(Const.GCM_KEY_NEW_MESSAGE)) {
                            String content = object.optString("content");

                            Context context = getBaseContext();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                    .setContentTitle(getResources().getString(R.string.app_name))
                                    .setContentText(content)
                                    .setAutoCancel(true)//cancel the notification when the user selects it
                                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                    .setLights(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), 2000, 1500);

                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            int id = Utils.getCachedInt(getBaseContext(), Const.CACHE_NOTIFICATION_ID, 0);
                            notificationManager.notify(id, builder.build());
                            Utils.cacheInt(getBaseContext(), Const.CACHE_NOTIFICATION_ID, id + 1);

                        }
                    } else if (user != null && user.getType() != null && user.getType() == UserType.CUSTOMER) { // -----customer-------
                        if (key.equals(Const.GCM_KEY_ACCEPT_TRIP)) {
                            // this is an accept request
                            // parse the json string
                            AcceptTripPayload tripPayload = gson.fromJson(json, AcceptTripPayload.class);

                            // cache this trip info
                            SavePrefs<TripInfo> savePrefs = new SavePrefs<TripInfo>(context, TripInfo.class);
                            savePrefs.save(tripPayload.getTripInfo(), Const.CACHE_LAST_TRIP_INFO);

                            // notify the customer verify activity if available
                            if (CustomerVerifyTripActivity.currentInstance != null) {
                                CustomerVerifyTripActivity.currentInstance.notifyDriverAccepted();
                            }

                            // open customer ride activity
                            Intent intent = new Intent(context, CustomerRideActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            // play horn sound
                            final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.horn);
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mediaPlayer.release();
                                }
                            });
                            mediaPlayer.start();

                        } else if (key.equals(Const.GCM_KEY_START_TRIP)) {
                            // start the trip
                            // show msg and finish the customer ride activity if available
                            Utils.showLongToast(context, R.string.start_trip_message);
                            if (CustomerRideActivity.currentInstance != null) {
                                CustomerRideActivity.currentInstance.finish();
                            }
                        } else if (key.equals(Const.GCM_KEY_DRIVER_CANCEL)) {
                            // cancel the trip
                            // show msg and finish the customer ride activity if available
                            Utils.showLongToast(context, R.string.customer_cancel_trip_message);
                            if (CustomerRideActivity.currentInstance != null) {
                                CustomerRideActivity.currentInstance.finish();
                            }
                        } else if (key.equals(Const.GCM_KEY_NO_DRIVER_FOUND)) {
                            // no drivers found
                            if (CustomerVerifyTripActivity.currentInstance != null) {
                                CustomerVerifyTripActivity.currentInstance.notifyNoDriversFound();
                            }
                        } else if (key.equals(Const.GCM_KEY_END_TRIP_CASH)) {
                            // cash trip ended
                            // get cost
                            float cost = 0;
                            try {
                                cost = Float.parseFloat(object.optString("content"));
                            } catch (Exception e) {
                            }

                            // start customer cash payment activity
                            Intent intent = new Intent(context, CustomerCashPaymentActivity.class);
                            intent.putExtra(Const.KEY_COST, cost);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (key.equals(Const.GCM_KEY_END_TRIP_CREDIT)) {
                            // credit trip ended
                            // get trip cost info object
                            EndCreditTripPayload endCreditTripPayload = gson.fromJson(json, EndCreditTripPayload.class);

                            // start customer credit payment activity
                            Intent intent = new Intent(context, CustomerAccountPaymentActivity.class);
                            intent.putExtra(Const.KEY_TRIP_COST_INFO, endCreditTripPayload.getTripCostInfo());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (key.equals(Const.GCM_KEY_NEW_MESSAGE)) {
                            String content = object.optString("content");

                            Context context = getBaseContext();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                    .setContentTitle(getResources().getString(R.string.app_name))
                                    .setContentText(content)
                                    .setAutoCancel(true)//cancel the notification when the user selects it
                                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                    .setLights(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), 2000, 1500);

                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            int id = Utils.getCachedInt(getBaseContext(), Const.CACHE_NOTIFICATION_ID, 0);
                            notificationManager.notify(id, builder.build());
                            Utils.cacheInt(getBaseContext(), Const.CACHE_NOTIFICATION_ID, id + 1);

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
