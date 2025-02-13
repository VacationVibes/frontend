package com.github.sviatoslavslysh.vacationvibes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.github.sviatoslavslysh.vacationvibes.activity.LocationPermissionActivity;
import com.github.sviatoslavslysh.vacationvibes.api.ApiClient;
import com.github.sviatoslavslysh.vacationvibes.activity.LoginActivity;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.utils.LocationHelper;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;

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
                proceedToApp();
            }

            @Override
            public void onPermissionDenied() {
                startBlockingActivity();
            }
        });
    }

    private void proceedToApp() {
        PreferencesManager preferencesManager = new PreferencesManager(this);

        if (preferencesManager.getTheme().equals("system")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (preferencesManager.getTheme().equals("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (preferencesManager.getTheme().equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if (preferencesManager.isLoggedIn()) {
            ApiClient.setAuthToken(preferencesManager.getToken());
            startActivity(new Intent(this, NavigationBarActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void startBlockingActivity() {
        Intent intent = new Intent(this, LocationPermissionActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.handlePermissionResult(requestCode, grantResults);
    }
}
