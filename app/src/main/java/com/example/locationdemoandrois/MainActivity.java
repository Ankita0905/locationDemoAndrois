package com.example.locationdemoandrois;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "MainActivity";
    LocationManager locationManager;
    LocationListener locationListener;

    Button btn_Start, btn_Stop;
    TextView location_textView;

    /*
    the other way to access location od the user
    with the FusedLocationProvider
     */

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location_textView = findViewById(R.id.txt_Location);
        btn_Start = findViewById(R.id.btn_Start);
        btn_Stop = findViewById(R.id.btn_Stop);

        // initialize the fusedLocationProviderClient

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // initialize location manager
        locationManager = (LocationManager) this.getSystemService((Context.LOCATION_SERVICE));
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(tag,"on location changed: " + location);
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
        {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            buildLocationRequest();
            buildLocationCallback();
        }

        //add listener for the buttons

        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                btn_Start.setEnabled(btn_Start.isEnabled());
                btn_Stop.setEnabled(true);
            }
        });

        btn_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                btn_Start.setEnabled(true);
                btn_Stop.setEnabled(!btn_Stop.isEnabled());
            }
        });

    }

    // this method is called when the user allow or deny the permission
    //  @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//        {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//            {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
//            }
//        }
//
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length <= 0)
        {
            //we recieve an empty array
            Log.i(tag,"onRequestPermissionResult: " + "User ineteratction was canceled");
        }
        else if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            //permission is granted
//            getLocation();
        }
        else
            {
            //permission is denied
            }
    }

    private void buildLocationRequest()
    {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    private void buildLocationCallback()
    {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location: locationResult.getLocations())
                {
                    location_textView.setText(String.valueOf(location.getLatitude()) + "/" + String.valueOf(location.getLongitude()));
                }
            }
        };

    }


}
