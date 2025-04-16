package com.github.sviatoslavslysh.vacationvibes.activity;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.sviatoslavslysh.vacationvibes.MainActivity;
import com.github.sviatoslavslysh.vacationvibes.api.ApiClient;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.model.AuthToken;
import com.github.sviatoslavslysh.vacationvibes.repository.AuthRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.AuthCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;
import com.github.sviatoslavslysh.vacationvibes.utils.InputValidator;
import com.github.sviatoslavslysh.vacationvibes.utils.UserManager;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView switchToRegisterText;
    private InputValidator inputValidator;
    private AuthRepository authRepository;
    private CardView loginButtonCardView;
    private ImageView vv_logo_background;
    private ImageView vv_logo_foreground;
    private ValueAnimator rotationAnimator;
    private PreferencesManager preferencesManager;
    private ImageView eyeIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferencesManager = new PreferencesManager(this);
        emailEditText = findViewById(R.id.email_label);
        passwordEditText = findViewById(R.id.password_label);
        loginButtonCardView = findViewById(R.id.card_view_sign_in);
        vv_logo_background = findViewById(R.id.vv_logo_background);
        vv_logo_foreground = findViewById(R.id.vv_logo_foreground);
        loginButton = findViewById(R.id.sign_in);
        switchToRegisterText = findViewById(R.id.set_sign_up);
        eyeIcon = findViewById(R.id.eye_icon);

        inputValidator = new InputValidator();
        authRepository = new AuthRepository(this);

        eyeIcon.setOnClickListener(v -> {
            if ((passwordEditText.getInputType() & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                eyeIcon.setImageResource(R.drawable.ic_eye);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eyeIcon.setImageResource(R.drawable.ic_eye_visible);
            }
            passwordEditText.setSelection(passwordEditText.getText().length());
        });

        switchToRegisterText.setOnClickListener(v -> switchToRegister());
        loginButton.setOnClickListener(v -> sendLoginRequest());

    }

    private void sendLoginRequest() {
        loginButton.setEnabled(false);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        boolean validInput = true;
        if (!inputValidator.isValidEmail(email)) {
            ToastManager.showToast(this, "Please enter valid email address");
            validInput = false;
        }
        if (!inputValidator.isValidPassword(password)) {
            ToastManager.showToast(this, "Password can not be shorter than 6 symbols");
            validInput = false;
        }

        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        loginButtonCardView.startAnimation(scaleUp);
        boolean finalValidInput = validInput;
        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation scaleDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
                loginButtonCardView.startAnimation(scaleDown);

                scaleDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!finalValidInput) {
                            loginButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        if (!finalValidInput) {
            return;
        }

        startAnimation();

        authRepository.login(email, password, new AuthCallback<AuthToken>() {
            @Override
            public void onSuccess(AuthToken authToken) {
                preferencesManager.setToken(authToken.getAccessToken());
                ApiClient.setAuthToken(authToken.getAccessToken());
                UserManager.getInstance().loadUser(LoginActivity.this);
                ToastManager.showToast(LoginActivity.this, "Login successful!");
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent = new Intent(LoginActivity.this, NavigationBarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }, 200);

                stopAnimation();
            }

            @Override
            public void onError(String errorMessage) {
                ToastManager.showToast(LoginActivity.this, errorMessage);
                stopAnimation();
            }
        });
    }

    private void switchToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void startAnimation() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(vv_logo_background, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(vv_logo_background, "scaleY", 1f, 1.2f);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(scaleX, scaleY);
        scaleUp.setDuration(200);

        scaleUp.start();

//        Animation scaleUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
//        vv_logo_background.startAnimation(scaleUp);

        rotationAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotationAnimator.setDuration(3000);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setInterpolator(null);

        rotationAnimator.addUpdateListener(animation -> {
            float currentRotation = (float) animation.getAnimatedValue();
            vv_logo_foreground.setRotation(currentRotation);

//            if (currentRotation % 60 == 0) {
//                stopAnimation();
//            }
        });

        rotationAnimator.start();
    }

    private void stopAnimation() {
//        Animation scaleDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
//        vv_logo_background.startAnimation(scaleDown);

        rotationAnimator.addUpdateListener(animation -> {
            int currentRotation = Math.round((float) animation.getAnimatedValue());
//            vv_logo_foreground.setRotation(currentRotation);
            if (currentRotation % 60 == 0) {
                rotationAnimator.cancel();
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(vv_logo_background, "scaleX", 1.2f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(vv_logo_background, "scaleY", 1.2f, 1f);

                AnimatorSet scaleUp = new AnimatorSet();
                scaleUp.playTogether(scaleX, scaleY);
                scaleUp.setDuration(200);
                scaleUp.start();

                loginButton.setEnabled(true);
            }
        });
    }
}