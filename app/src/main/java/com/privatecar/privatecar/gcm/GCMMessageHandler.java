package com.privatecar.privatecar.gcm;

/**
 * Created by Basim Alamuddin on 13/12/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.CustomerAccountPaymentActivity;
import com.privatecar.privatecar.activities.CustomerCashPaymentActivity;
import com.privatecar.privatecar.activities.CustomerRideActivity;
import com.privatecar.privatecar.activities.CustomerVerifyTripActivity;
import com.privatecar.privatecar.activities.DriverTripInfoActivity;
import com.privatecar.privatecar.activities.DriverTripRequestActivity;
import com.privatecar.privatecar.models.entities.Message;
import com.privatecar.privatecar.models.entities.TripCostInfo;
import com.privatecar.privatecar.models.entities.TripInfo;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.payload.AcceptTripPayload;
import com.privatecar.privatecar.models.payload.EndCreditTripPayload;
import com.privatecar.privatecar.models.payload.TripRequestPayload;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.SavePrefs;
import com.privatecar.privatecar.utils.Utils;

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
                    if (user != null && user.getType() != null && user.getType() == UserType.DRIVER) {
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

                            // play horn sound
                            final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.horn);
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mediaPlayer.release();
                                }
                            });
                            mediaPlayer.start();
                        } else if (key.equals(Const.GCM_KEY_CUSTOMER_CANCEL)) {
                            // cancel the trip
                            // show msg and finish the driver ride activity if available
                            Utils.showLongToast(context, R.string.driver_cancel_trip_message);
                            if (DriverTripInfoActivity.currentInstance != null) {
                                DriverTripInfoActivity.currentInstance.finish();
                            }
                        }
                    } else if (user != null && user.getType() != null && user.getType() == UserType.CUSTOMER) {
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
                            } catch (Exception e) {}

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
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
