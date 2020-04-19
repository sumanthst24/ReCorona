package com.example.android.recorona;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap,oth;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME=1000;
    private final long MIN_DIST=5;

    private LatLng latLng;

  int[]  latitude_array=new int[]{26,16,17,28,28,28,28,28,28,28,28,28,28,28,27,27,27,27,27,27,22,
          22,22,22,19,19,19,19,19,19,19,19,19,19,19,19,19,18,18,18,18,15,15,15,15,8,11,
          11,11,13,17,18,22,23,25,25,22,19,23,21,30,34,30,27,11,13};
   int[] longitude_array=new int[]{77,77,79,77,80,76,76,77,77,77,77,77,77,77,75,75,75,75,
           75,75,71,71,71,71,73,73,73,73,73,73,73,73,73,74,74,74,74,73,73,73,73,78,78,78,
           78,76,78,78,78,77,83,83,88,88,86,85,81,79,77,75,78,76,75,79,77,79};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        oth=googleMap;

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        int i;
        for (i=0;i<latitude_array.length;i++){

            LatLng looper = new LatLng(latitude_array[i],longitude_array[i]);
            mMap.addMarker(new MarkerOptions().position(looper).title("RC "+i+1));
            mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            looper,
                            5f
                    )
            );

            mMap.addCircle(
                   new  CircleOptions()
                            .center(looper)
                            .radius(5000)
                            .strokeWidth(3f)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(70, 150, 50, 50))


            );
            mMap.setBuildingsEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);



        }

        oth.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("My location");

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                oth.addMarker(markerOptions);
                //oth.clear();
                checkDanger(latLng.latitude,latLng.longitude);
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    oth.addMarker(new MarkerOptions().position(latLng).title("My Current Position"));
                    oth.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    double lat=location.getLatitude();
                    double lon=location.getLongitude();

                }
                catch (SecurityException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void checkDanger(double lat,double lon){
        int lati=(int)Math.round(lat);
        int longi=(int)Math.round(lon);
        int i=0;
        boolean val=false;
        for(i=0;i<latitude_array.length;i++){
            if(latitude_array[i]==lati && longitude_array[i]==longi){
                val=true;
            }
        }
        if(val){
            Toast.makeText(this,"You are in Corona Alert Zone",Toast.LENGTH_SHORT).show();
        }
    }

}
