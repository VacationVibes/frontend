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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView switchToLoginText;
    private ExecutorService executorService;
    private InputValidator inputValidator;
    private AuthRepository authRepository;
    private CardView registerButtonCardView;
    private ImageView vv_logo_background;
    private ImageView vv_logo_foreground;
    private ValueAnimator rotationAnimator;
    private PreferencesManager preferencesManager;
    private ImageView eyeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        preferencesManager = new PreferencesManager(this);
        nameEditText = findViewById(R.id.name_label);
        emailEditText = findViewById(R.id.email_label);
        passwordEditText = findViewById(R.id.password_label);
        registerButton = findViewById(R.id.register);
        switchToLoginText = findViewById(R.id.set_login);
        registerButtonCardView = findViewById(R.id.card_view_register);
        vv_logo_background = findViewById(R.id.vv_logo_background);
        vv_logo_foreground = findViewById(R.id.vv_logo_foreground);
        eyeIcon = findViewById(R.id.eye_icon); // Eye icon

        inputValidator = new InputValidator();
        authRepository = new AuthRepository(this);
        executorService = Executors.newSingleThreadExecutor();

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

        switchToLoginText.setOnClickListener(v -> switchToLogin());
        registerButton.setOnClickListener(v -> sendRegisterRequest());
    }

    private void sendRegisterRequest() {
        registerButton.setEnabled(false);

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean validInput = true;
        if (!inputValidator.isValidName(name)) {
            ToastManager.showToast(this, "Please enter your name");
            validInput = false;
        }
        if (!inputValidator.isValidEmail(email)) {
            ToastManager.showToast(this, "Please enter valid email address");
            validInput = false;
        }
        if (!inputValidator.isValidPassword(password)) {
            ToastManager.showToast(this, "Password can not be shorter than 6 symbols");
            validInput = false;
        }
        boolean finalValidInput = validInput;

        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        registerButtonCardView.startAnimation(scaleUp);
        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation scaleDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
                registerButtonCardView.startAnimation(scaleDown);

                scaleDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!finalValidInput) {
                            registerButton.setEnabled(true);
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

        authRepository.register(email, password, name, new AuthCallback<AuthToken>() {
            @Override
            public void onSuccess(AuthToken authToken) {
                preferencesManager.setToken(authToken.getAccessToken());
                ApiClient.setAuthToken(authToken.getAccessToken());
                UserManager.getInstance().loadUser(RegisterActivity.this);

                // todo manage animations
                stopAnimation();
                ToastManager.showToast(RegisterActivity.this, "Registration successful!");
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent = new Intent(RegisterActivity.this, NavigationBarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }, 200);

            }

            @Override
            public void onError(String errorMessage) {
                stopAnimation();
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

                registerButton.setEnabled(true);
            }
        });
    }

}
