package com.hababk.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.hababk.restaurant.R;

import java.io.IOException;
import java.util.List;

//import com.hababk.restaurant.activity.HomeActivity.GPSTracker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private LocationManager locationManager;
    //    private GPSTracker gpsTracker;
//    private GPSTracker gpsTracker;
    private Location mLocation;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    //    private TextView resutText;
    Button submit;
    private Double latitude, longitude;
    String address;
    private LatLng latLng;
    List<Address> addressList;
    ImageView mapfocus;
//    String current_lat = "", current_lng = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < 22)
            setStatusBarTranslucent(false);
        else
            setStatusBarTranslucent(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        resutText = (TextView) findViewById(R.id.dragg_result);
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();

        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        mapfocus = (ImageView) findViewById(R.id.mapfocus);
        mapfocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation2();
//                onLocationChanged();
            }
        });
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        configureCameraIdle();
    }



    private void getCurrentLocation2() {

        latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                mapfocus.setVisibility(View.INVISIBLE);
//            }
//        }

//        configureCameraIdle();
    }




    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                latLng = mMap.getCameraPosition().target;
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
//                mMap.animateCamera(cameraUpdate);
                Geocoder geocoder = new Geocoder(MapsActivity.this);

                try {
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        address = addressList.get(0).getAddressLine(0);
                        latitude = addressList.get(0).getLatitude();
                        longitude = addressList.get(0).getLongitude();
//                        if (!locality.isEmpty() && !country.isEmpty())
//                            resutText.setText(locality + "  " + country);
//                        Toast.makeText(MapsActivity.this,addressList,Toast.LENGTH_LONG).show();
//                        Toast.makeText(MapsActivity.this,latitude + "  " + longitude,Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(MapsActivity.this,"NOT GPS", Toast.LENGTH_LONG).show();
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);

        latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mMap.animateCamera(cameraUpdate);

//        Toast.makeText(MapsActivity.this,"GPS", Toast.LENGTH_LONG).show();

//        latLng = new LatLng(latitude, longitude);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("I'm here..."));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void submit() {
        Intent intent = new Intent();
//        SharedPreferenceUtil sharedPreferences = new SharedPreferenceUtil(this);
//        sharedPreferences.setDoublePreference("latitude", latitude);
//        sharedPreferences.setDoublePreference("longitude", longitude);
//        sharedPreferences.setStringPreference("address", address);

        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("address", address);
        setResult(Activity.RESULT_OK, intent);
        finish();
//        Bundle bundle = new Bundle();
//        intent.putExtras("latitude", latitude);
//        bundle.putDouble("longitude", longitude);
//        bundle.putDouble("address", address);
//        bundle.putBoolean("mapActivity", true);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

}
