package com.github.sviatoslavslysh.vacationvibes.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;


public class LocationHelper {
    private static LocationHelper instance;
    private final Context context;
    private final LocationManager locationManager;
    private Location userLocation;
    private PermissionCallback permissionCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @SuppressLint("MissingPermission")
    private LocationHelper(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        if (hasLocationPermission()) {
            this.userLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }

    public static synchronized LocationHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LocationHelper(context);
        }
        return instance;
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission(@NonNull Activity activity, @NonNull PermissionCallback callback) {
        this.permissionCallback = callback;
        if (hasLocationPermission()) {
            callback.onPermissionGranted();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void handlePermissionResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && permissionCallback != null) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionCallback.onPermissionGranted();
            } else {
                permissionCallback.onPermissionDenied();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void requestUserLocation() {
        if (!hasLocationPermission()) return;

        try {
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGpsEnabled) {
                Log.e("LocationHelper", "GPS is not enabled");
            } else {
                Log.e("LocationHelper", "GPS is enabled");
            }

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation != null) {
                Log.d("LocationHelper", "Last known location from NETWORK_PROVIDER: " + lastKnownLocation.toString());
            } else {
                Log.d("LocationHelper", "No last known location from NETWORK_PROVIDER.");
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 500, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    System.out.println("onLocationChanged");
                    userLocation = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    System.out.println("onStatusChanged");
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    System.out.println("onProviderEnabled");
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    System.out.println("onProviderDisabled");
                }
            });
        } catch (SecurityException e) {
            Log.e("LocationHelper", "Error requesting location updates", e);
        }

    }

    public double calculateDistanceTo(double latitude, double longitude) {
        if (getUserLocation() == null) return -1;

        Location targetLocation = new Location("");
        targetLocation.setLatitude(latitude);
        targetLocation.setLongitude(longitude);

        float distanceInMeters = userLocation.distanceTo(targetLocation);  // distance in meters
        return distanceInMeters * 0.000621371;  // returns distance in miles
    }

    private Location getUserLocation() {
        return userLocation;
    }

    public interface PermissionCallback {
        void onPermissionGranted();

        void onPermissionDenied();
    }
}
