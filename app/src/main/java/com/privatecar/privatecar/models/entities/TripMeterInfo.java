package com.privatecar.privatecar.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by basim on 12/4/16.
 */
public class TripMeterInfo implements Parcelable {
    private int time; // in minutes
    private int distance; // in meters
    private int waitingSpeedTime; // in minutes

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getWaitingSpeedTime() {
        return waitingSpeedTime;
    }

    public void setWaitingSpeedTime(int waitingSpeedTime) {
        this.waitingSpeedTime = waitingSpeedTime;
    }

    public TripMeterInfo() {

    }

    public TripMeterInfo(Parcel in) {
        time = in.readInt();
        distance = in.readInt();
        waitingSpeedTime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(time);
        dest.writeInt(distance);
        dest.writeInt(waitingSpeedTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TripMeterInfo> CREATOR = new Parcelable.Creator<TripMeterInfo>() {
        @Override
        public TripMeterInfo createFromParcel(Parcel in) {
            return new TripMeterInfo(in);
        }

        @Override
        public TripMeterInfo[] newArray(int size) {
            return new TripMeterInfo[size];
        }
    };
}
