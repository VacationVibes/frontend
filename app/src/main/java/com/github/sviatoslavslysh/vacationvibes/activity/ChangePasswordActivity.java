package com.github.sviatoslavslysh.vacationvibes.activity;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.repository.AuthRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.AuthCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Button changePasswordButton;
    private AuthRepository authRepository;
    private ImageView eyeIconCurrent, eyeIconNew, eyeIconConfirm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        PreferencesManager preferencesManager = new PreferencesManager(this); // Ensure this constructor exists
        AuthRepository authRepository = new AuthRepository(this);

        authRepository = new AuthRepository(this);

        currentPasswordEditText = findViewById(R.id.current_password);
        newPasswordEditText = findViewById(R.id.new_password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        changePasswordButton = findViewById(R.id.change_password_button);
        eyeIconCurrent = findViewById(R.id.eye_icon_current);
        eyeIconNew = findViewById(R.id.eye_icon_new);
        eyeIconConfirm = findViewById(R.id.eye_icon_confirm);

        setupPasswordToggle(eyeIconCurrent, currentPasswordEditText);
        setupPasswordToggle(eyeIconNew, newPasswordEditText);
        setupPasswordToggle(eyeIconConfirm, confirmPasswordEditText);

        changePasswordButton.setOnClickListener(v -> handleChangePassword());
    }

    private void setupPasswordToggle(ImageView eyeIcon, EditText passwordField) {
        eyeIcon.setOnClickListener(v -> {
            if ((passwordField.getInputType() & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                eyeIcon.setImageResource(R.drawable.ic_eye);
            } else {
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eyeIcon.setImageResource(R.drawable.ic_eye_visible);
            }
            passwordField.setSelection(passwordField.getText().length());
        });
    }

    private void handleChangePassword() {
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            ToastManager.showToast(this, "Please enter your current password");
            return;
        }
        if (newPassword.length() < 6) {
            ToastManager.showToast(this, "New password must be at least 6 characters");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            ToastManager.showToast(this, "Passwords do not match");
            return;
        }

        authRepository.changePassword(currentPassword, newPassword, new AuthCallback<String>() {
            @Override
            public void onSuccess(String response) {
                ToastManager.showToast(ChangePasswordActivity.this, "Password changed successfully!");
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                ToastManager.showToast(ChangePasswordActivity.this, errorMessage);
            }
        });
    }
}
