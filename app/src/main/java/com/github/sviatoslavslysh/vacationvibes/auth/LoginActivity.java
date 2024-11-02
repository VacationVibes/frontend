package com.github.sviatoslavslysh.vacationvibes.auth;

import static com.github.sviatoslavslysh.vacationvibes.MainActivity.AUTH_ENDPOINT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;
import com.github.sviatoslavslysh.vacationvibes.utils.InputValidator;

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

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView switchToRegisterText;
    private PreferencesManager preferencesManager;
    private ExecutorService executorService;
    private InputValidator inputValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_label);
        passwordEditText = findViewById(R.id.password_label);

        preferencesManager = new PreferencesManager(this);
        inputValidator = new InputValidator();
        executorService = Executors.newSingleThreadExecutor();

        loginButton = findViewById(R.id.sign_in);
        switchToRegisterText = findViewById(R.id.set_sign_up);
        switchToRegisterText.setOnClickListener(v -> switchToRegister());
        loginButton.setOnClickListener(v -> sendLoginRequest());

    }

    private void sendLoginRequest() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (!inputValidator.isValidEmail(email)) {
            ToastManager.showToast(this, "Please enter valid email address");
            return;
        }
        if (!inputValidator.isValidPassword(password)) {
            ToastManager.showToast(this, "Password can not be shorter than 6 symbols");
            return;
        }

        OkHttpClient client = new OkHttpClient();

        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(AUTH_ENDPOINT + "/login")
                .post(body)
                .addHeader("Accept", "application/json")
                .build();

        executorService.execute(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    System.out.println("Response: " + jsonObject);
                    if (jsonObject.getString("token_type").equals("Bearer")) {
                        preferencesManager.setToken("Bearer " + jsonObject.getString("access_token"));
                    }
                    runOnUiThread(() -> ToastManager.showToast(getApplicationContext(), "login successful"));
                    Intent intent = new Intent(this, NavigationBarActivity.class);
                    startActivity(intent);
                    finish();

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

    private void switchToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}