package com.github.sviatoslavslysh.vacationvibes.auth;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.model.AuthToken;
import com.github.sviatoslavslysh.vacationvibes.repository.AuthRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.AuthCallback;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView switchToLoginText;
    private ExecutorService executorService;
    private InputValidator inputValidator;
    private AuthRepository authRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.name_label);
        emailEditText = findViewById(R.id.email_label);
        passwordEditText = findViewById(R.id.password_label);
        loginButton = findViewById(R.id.register);
        switchToLoginText = findViewById(R.id.set_login);

        inputValidator = new InputValidator();
        authRepository = new AuthRepository(new PreferencesManager(this));
        executorService = Executors.newSingleThreadExecutor();

        switchToLoginText.setOnClickListener(v -> switchToLogin());
        loginButton.setOnClickListener(v -> sendRegisterRequest());
    }

    private void sendRegisterRequest() {
        String name = nameEditText.getText().toString().trim();
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

        authRepository.register(email, password, name, new AuthCallback<AuthToken>() {
            @Override
            public void onSuccess(AuthToken authToken) {
                // todo manage animations
                ToastManager.showToast(RegisterActivity.this, "Registration successful!");
                Intent intent = new Intent(RegisterActivity.this, NavigationBarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                ToastManager.showToast(RegisterActivity.this, errorMessage);
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
