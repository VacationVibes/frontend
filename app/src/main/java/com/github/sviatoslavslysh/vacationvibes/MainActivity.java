package com.github.sviatoslavslysh.vacationvibes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sviatoslavslysh.vacationvibes.auth.LoginActivity;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("is_logged_in", true); // changed

        if (isLoggedIn) {
            // todo validate token
            Intent intent = new Intent(this, NavigationBarActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
