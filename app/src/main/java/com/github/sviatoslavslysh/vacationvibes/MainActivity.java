package com.github.sviatoslavslysh.vacationvibes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.sviatoslavslysh.vacationvibes.api.ApiClient;
import com.github.sviatoslavslysh.vacationvibes.auth.LoginActivity;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.utils.LocationHelper;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;

public class MainActivity extends AppCompatActivity {
    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requests user's location
        locationHelper = LocationHelper.getInstance(this);
        locationHelper.requestLocationPermission(this, new LocationHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                locationHelper.requestUserLocation();
            }

            @Override
            public void onPermissionDenied() {
                // todo block app until user enables GPS permission
                ToastManager.showToast(MainActivity.this, "Permission denied");
            }
        });


        PreferencesManager preferencesManager = new PreferencesManager(this);

        // checks if user is logged in or not
        if (preferencesManager.isLoggedIn()) {
            ApiClient.setAuthToken(preferencesManager.getToken());
            Intent intent = new Intent(this, NavigationBarActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.handlePermissionResult(requestCode, grantResults);
    }
}
