package com.privatecar.privatecar.gcm;

/**
 * Created by Basim Alamuddin on 13/12/2015.
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.CustomerRideActivity;
import com.privatecar.privatecar.activities.DriverTripRequestActivity;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.payload.AcceptTripPayload;
import com.privatecar.privatecar.models.payload.TripRequestPayload;
import com.privatecar.privatecar.utils.AppUtils;

import org.json.JSONObject;

public class GCMMessageHandler extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        try {
            // create gson object and get json string
            Gson gson = new Gson();
            String json = data.getString("message");

            Log.e(Const.LOG_TAG, "GCM Message: " + json);

            // get the key
            JSONObject object = new JSONObject(json);
            String key = object.optString("key");

            // check logged in user type
            User user = AppUtils.getCachedUser(this);
            if (user != null && user.getType() != null && user.getType() == UserType.DRIVER) {

                // this is a driver user
                // check the key
                if (key.equals(Const.GCM_KEY_TRIP_REQUEST)) {
                    // this is a trip request
                    // parse the json string
                    TripRequestPayload requestPayload = gson.fromJson(json, TripRequestPayload.class);

                    // open trip request activity
                    Intent intent = new Intent(this, DriverTripRequestActivity.class);
                    intent.putExtra(Const.KEY_TRIP_REQUEST, requestPayload.getTripRequest());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    // play horn sound
                    final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.horn);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                        }
                    });
                    mediaPlayer.start();
                }
            } else if (user != null && user.getType() != null && user.getType() == UserType.CUSTOMER) {
                if (key.equals("accepttrip")) {
                    // this is an accept request
                    // parse the json string
                    AcceptTripPayload tripPayload = gson.fromJson(json, AcceptTripPayload.class);


                    // open customer ride activity
                    Intent intent = new Intent(this, CustomerRideActivity.class);
                    intent.putExtra(Const.KEY_TRIP_INFO, tripPayload.getTripInfo());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
