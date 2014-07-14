package io.lakeviewlabs.radar;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by spencermorris on 7/12/14.
 */
public class Utils {

    public static boolean setLastKnownLocationToUserData(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location passiveLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (gpsLocation != null) {
            System.out.println("LAST KNOWN LOCATION GPS:"+gpsLocation);
            UserData.instance().setLatLng(activity.getBaseContext(), new LatLng(gpsLocation.getLatitude(), gpsLocation.getLongitude()));
            return true;
        } else if (networkLocation != null) {
            System.out.println("LAST KNOWN LOCATION NETWORK:"+networkLocation);
            UserData.instance().setLatLng(activity.getBaseContext(), new LatLng(gpsLocation.getLatitude(), gpsLocation.getLongitude()));
            return true;
        } else if (passiveLocation != null) {
            System.out.println("LAST KNOWN LOCATION PASSIVE:"+passiveLocation);
            UserData.instance().setLatLng(activity.getBaseContext(), new LatLng(passiveLocation.getLatitude(), passiveLocation.getLongitude()));
            return true;
        } else {
            System.out.println("ALL LAST KNOWN LOCATIONS NULL, GETTING ONE LOCATION");
            return false;
        }
    }
}
