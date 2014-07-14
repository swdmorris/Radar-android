package io.lakeviewlabs.radar;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by spencermorris on 7/12/14.
 */
public class UserData {
    private final String sharedPrefsName = "UserData";
    private final String sharedPrefsLatitude = "latitude";
    private final String sharedPrefsLongitude = "longitude";

    private static UserData instance = null;

    protected UserData() { /*Exists only to defeat instantiation*/ }

    public static synchronized UserData instance() {
        if(instance == null) {
            instance = new UserData();
        }

        return instance;
    }

    // LATITUDE
    public LatLng latLng(Context context) {
        float lat = context.getSharedPreferences(sharedPrefsName, 0).getFloat(sharedPrefsLatitude, 0.0f);
        float lng = context.getSharedPreferences(sharedPrefsName, 0).getFloat(sharedPrefsLongitude, 0.0f);

        if (lat == 0.0 && lng == 0.0) {
            return null;
        } else {
            return new LatLng(lat, lng);
        }
    }

    public void setLatLng(Context context, LatLng latLng) {
        context.getSharedPreferences(sharedPrefsName, 0).edit().putFloat(sharedPrefsLatitude, (float) latLng.latitude).commit();
        context.getSharedPreferences(sharedPrefsName, 0).edit().putFloat(sharedPrefsLongitude, (float) latLng.longitude).commit();
    }
}
