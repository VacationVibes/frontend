package com.github.sviatoslavslysh.vacationvibes.auth;

import static com.github.sviatoslavslysh.vacationvibes.MainActivity.AUTH_ENDPOINT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;
import com.github.sviatoslavslysh.vacationvibes.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView switchToLoginText;
    private PreferencesManager preferencesManager;
    private ExecutorService executorService;
    private Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.email_label);
        passwordEditText = findViewById(R.id.password_label);
        loginButton = findViewById(R.id.register);
        switchToLoginText = findViewById(R.id.set_login);

        preferencesManager = new PreferencesManager(this);
        utils = new Utils();
        executorService = Executors.newSingleThreadExecutor();

        switchToLoginText.setOnClickListener(v -> switchToLogin());
        loginButton.setOnClickListener(v -> sendRegisterRequest());
    }

    private void sendRegisterRequest() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (!utils.isValidEmail(email)) {
            ToastManager.showToast(this, "Please enter valid email address");
            return;
        }
        if (!utils.isValidPassword(password)) {
            ToastManager.showToast(this, "Password can not be shorter than 6 symbols");
            return;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject payload = new JSONObject();
        try {  // todo add name
            payload.put("email", email);
            payload.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(AUTH_ENDPOINT + "/register")
                .post(body)
                .addHeader("Accept", "application/json")
                .build();

        executorService.execute(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    System.out.println("Response: " + jsonObject);
                    if (jsonObject.getString("token_type").equals("Bearer")) {
                        preferencesManager.setToken("Bearer " + jsonObject.getString("access_token"));
                    }
                    runOnUiThread(() -> ToastManager.showToast(getApplicationContext(), "register successful"));

                } else if (response.code() == 422) {
                    assert response.body() != null;
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    runOnUiThread(() -> {
                        try {
                            ToastManager.showToast(getApplicationContext(), jsonObject.getJSONArray("detail").getJSONObject(0).getString("msg"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else if (response.code() == 400) {  // normal error from backend
                    assert response.body() != null;
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    runOnUiThread(() -> {
                        try {
                            ToastManager.showToast(getApplicationContext(), jsonObject.getString("detail"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    runOnUiThread(() -> ToastManager.showToast(getApplicationContext(), "Unknown error\nStatus code: " + response.code()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void switchToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
