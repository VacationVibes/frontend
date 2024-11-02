package com.github.sviatoslavslysh.vacationvibes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.github.sviatoslavslysh.vacationvibes.auth.LoginActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private static final String BASE_URL = "http://192.168.1.19:80";
    public static final String AUTH_ENDPOINT = BASE_URL + "/auth";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            // todo validate token
            setContentView(R.layout.activity_main);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

//        etEmail = findViewById(R.id.email);
//        TextView signInTextView = findViewById(R.id.sign_in);
//        signInTextView.setOnClickListener(v -> {
//            // Здесь обработчик нажатия на "Sign In"
//            Toast.makeText(this, "Sign In clicked", Toast.LENGTH_SHORT).show();
//        });

//        etPassword = findViewById(R.id.passwordInput);
//        btnRegister = findViewById(R.id.btnRegister);

//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = etEmail.getText().toString().trim();
//                String password = etPassword.getText().toString().trim();
//
//                if (email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(MainActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
//                } else if (!isValidEmail(email)) {
//                    Toast.makeText(MainActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
//                } else if (password.length() < 6) {
//                    Toast.makeText(MainActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//                    sendRegistrationData(email, password);
//
//                }
//            }
//        });
    }

//    private void sendRegistrationData(String email, String password) {
//        new Thread(() -> {
//            try {
//                URL url = new URL(AUTH_ENDPOINT + "/login");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type", "application/json; utf-8");
//                connection.setDoOutput(true);
//
//                JSONObject jsonParam = new JSONObject();
//                jsonParam.put("email", email);
//                jsonParam.put("password", password);
//
//                try (OutputStream os = connection.getOutputStream()) {
//                    byte[] input = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
//                    os.write(input, 0, input.length);
//                }
//
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    runOnUiThread(() ->
//                            Toast.makeText(MainActivity.this, "Server Registration Successful!", Toast.LENGTH_SHORT).show());
//                } else {
//                    runOnUiThread(() ->
//                            Toast.makeText(MainActivity.this, "Server Registration Failed!", Toast.LENGTH_SHORT).show());
//                }
//
//                connection.disconnect();
//            } catch (Exception e) {
//                e.printStackTrace();
//                runOnUiThread(() ->
//                        Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show());
//            }
//        }).start();
//    }
//
//    private boolean isValidEmail(CharSequence email) {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
}
