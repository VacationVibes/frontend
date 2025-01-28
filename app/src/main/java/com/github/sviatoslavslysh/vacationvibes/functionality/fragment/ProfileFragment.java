package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.sviatoslavslysh.vacationvibes.MainActivity;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.api.ApiClient;
import com.github.sviatoslavslysh.vacationvibes.auth.LoginActivity;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.model.AuthToken;
import com.github.sviatoslavslysh.vacationvibes.model.HomeViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.ProfileViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.User;
import com.github.sviatoslavslysh.vacationvibes.repository.AuthRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.AuthCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    private PreferencesManager preferencesManager;
    private AuthRepository authRepository;
    private ProfileViewModel profileViewModel;

    private TextView userNameTextView;
    private TextView likeCountTextView;
    private  TextView dislikeCountTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        userNameTextView = rootView.findViewById(R.id.user_name);
        likeCountTextView = rootView.findViewById(R.id.like_count);
        dislikeCountTextView = rootView.findViewById(R.id.dislike_count);

        preferencesManager = new PreferencesManager(this.requireContext());
        authRepository = new AuthRepository(new PreferencesManager(requireContext()));
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        Button logoutButton = rootView.findViewById(R.id.button_log_out);
        logoutButton.setOnClickListener(v -> logout());
        if (profileViewModel.getId() == null) {
            loadCurrentUser();
        }
        return rootView;
    }

    public void logout() {
        preferencesManager.removeToken();
        Intent intent = new Intent(this.requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        requireActivity().finish();
    }

    public void loadCurrentUser() {
        if (profileViewModel.isAwaitingResponse()) {
            return;
        }

        profileViewModel.setAwaitingResponse(true);

        authRepository.getCurrentUser(new AuthCallback<User>() {
            @Override
            public void onSuccess(User user) {
                profileViewModel.setAwaitingResponse(false);
                profileViewModel.setName(user.getName());
                profileViewModel.setEmail(user.getEmail());
                profileViewModel.setId(user.getId());
                userNameTextView.setText(user.getName());

                // todo finish
//                likeCountTextView.setText(String.valueOf(user.getLikes()));
//                dislikeCountTextView.setText(String.valueOf(user.getDislikes()));
            }

            @Override
            public void onError(String errorMessage) {
                profileViewModel.setAwaitingResponse(false);
                ToastManager.showToast(requireContext(), errorMessage);
            }
        });
    }
}
