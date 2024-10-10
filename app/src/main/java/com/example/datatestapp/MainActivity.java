package com.example.datatestapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.datatestapp.DB.Connection;
import com.example.datatestapp.DB.Interfase;
import com.example.datatestapp.DB.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private Items item;
    private LocationManager locationManager;
    private boolean isTracking = false;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = new Items();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.startButton);
        DialogUserName myDialog = new DialogUserName();
        myDialog.show(getSupportFragmentManager(), "123");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            Log.e(TAG, "LocationManager is null");
            Toast.makeText(this, "Unable to access location services", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "App started");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTracking) {
                    button.setText("STOP");
                    requestLocationUpdates();
                    isTracking = true;
                } else {
                    button.setText("START");
                    stopLocationUpdates();
                    isTracking = false;
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                LocationRequest mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(200);
                mLocationRequest.setFastestInterval(150);

                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            Log.e(TAG, "Location result is null");
                            return;
                        }

                        for (Location location : locationResult.getLocations()) {
                            Log.d(TAG, "Location: " + location.getLatitude() + ", " + location.getLongitude());
                            String dateTime = item.getDateTime();
                            Log.d(TAG, "DateTime: " + dateTime);

                            Call<Response> call = Connection.getConnection().create(Interfase.class)
                                    .adduser(item.getName(), Double.toString(location.getLatitude()),
                                            Double.toString(location.getLongitude()),
                                            Double.toString(location.getAltitude()),
                                            item.getDateTime());


                            call.enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Server error: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Network error: " + t.getMessage());
                                }
                            });
                        }
                    }
                };

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
                Log.d(TAG, "Requesting location updates");
            } else {
                Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    private void stopLocationUpdates() {
        if (mLocationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Log.d(TAG, "Location updates stopped");
        }
    }

    private boolean checkPermissions() {
        boolean coarseLocationGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fineLocationGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        Log.d(TAG, "Coarse Location Permission: " + coarseLocationGranted);
        Log.d(TAG, "Fine Location Permission: " + fineLocationGranted);

        return coarseLocationGranted && fineLocationGranted;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
        Log.d(TAG, "Requesting permissions");
    }

    private boolean isLocationEnabled() {
        if (locationManager == null) {
            Log.e(TAG, "LocationManager is null in isLocationEnabled");
            return false;
        }

        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d(TAG, "GPS Enabled: " + gpsEnabled);
        Log.d(TAG, "Network Enabled: " + networkEnabled);

        return gpsEnabled || networkEnabled;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            }
        }
    }

    public void createNewItem(Items item) {
        this.item = item;
        Log.d(TAG, "New item created: " + this.item.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions() && isTracking) {
            requestLocationUpdates();
        }
    }
}
