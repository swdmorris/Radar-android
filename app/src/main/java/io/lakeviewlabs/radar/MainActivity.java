package io.lakeviewlabs.radar;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

public class MainActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageView mOverlayImageView;
    private final String TAG = "MainActivity";
    private int mZoomLevel = 8;//
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private float kMAX_DIMENSION = 640f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOverlayImageView = (ImageView) findViewById(R.id.overlay_imageview);
        setUpMapIfNeeded();
        setupLocationServices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
        LatLng latLng = UserData.instance().latLng(this);
        if (latLng == null) {
            Log.d(TAG, "no location");
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mZoomLevel));
        }

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                setupImageForMapBounds();
            }
        });
    }

    private void setupLocationServices() {
        if (Utils.setLastKnownLocationToUserData(this)) {
            Log.d(TAG, "NON-NULL LAST KNOWN LOCATION");
        } else {
            getOneLocationUpdate();
        }
    }

    private void setupImageForMapBounds() {
        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        double left = vr.latLngBounds.southwest.longitude;
        double top = vr.latLngBounds.northeast.latitude;
        double right = vr.latLngBounds.northeast.longitude;
        double bottom = vr.latLngBounds.southwest.latitude;

        int width = mOverlayImageView.getWidth();
        int height = mOverlayImageView.getHeight();

        float ratio = kMAX_DIMENSION / Math.max(width, height);
        width = (int) ((float) width * ratio);
        height = (int) ((float) height * ratio);

        String path = "http://api.wunderground.com/api/f1a0c22b31dfc144/radar/image.gif?maxlat="+right+
                "&maxlon="+top+"&minlat="+left+"&minlon="+bottom+"&width="+width+"&height="+height+"&newmaps=1&num=5";
        Log.d(TAG, "NEW IMAGE PATH: "+path);
        Glide.load(path).into(mOverlayImageView);
    }

    private void getOneLocationUpdate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d(TAG, "FOUND LOCATION:"+location);
                UserData.instance().setLatLng(getApplicationContext(), new LatLng(location.getLatitude(), location.getLongitude()));
                mLocationManager.removeUpdates(mLocationListener);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
    }
}
