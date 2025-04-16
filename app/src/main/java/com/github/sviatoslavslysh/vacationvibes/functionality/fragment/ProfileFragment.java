//package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.github.sviatoslavslysh.vacationvibes.MainActivity;
//import com.github.sviatoslavslysh.vacationvibes.R;
//import com.github.sviatoslavslysh.vacationvibes.model.ProfileViewModel;
//import com.github.sviatoslavslysh.vacationvibes.model.User;
//import com.github.sviatoslavslysh.vacationvibes.repository.AuthRepository;
//import com.github.sviatoslavslysh.vacationvibes.utils.AuthCallback;
//import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
//import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;
//
//
//public class ProfileFragment extends Fragment {
//    private PreferencesManager preferencesManager;
//    private AuthRepository authRepository;
//    private ProfileViewModel profileViewModel;
//
//    private TextView userNameTextView;
//    private TextView likeCountTextView;
//    private TextView dislikeCountTextView;
//    private User currentUser;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
//        userNameTextView = rootView.findViewById(R.id.user_name);
//        likeCountTextView = rootView.findViewById(R.id.like_count);
//        dislikeCountTextView = rootView.findViewById(R.id.dislike_count);
//
//        preferencesManager = new PreferencesManager(this.requireContext());
//        authRepository = new AuthRepository(requireContext());
//        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
//
//        Button logoutButton = rootView.findViewById(R.id.button_log_out);
//        Button settingsButton = rootView.findViewById(R.id.button_settings);
//
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new SettingsFragment())
//                        .commit();
//
//            }
//        });
//        logoutButton.setOnClickListener(v -> logout());
//        if (profileViewModel.getId() == null) {
//            loadCurrentUser();
//        } else {
//            showUser(profileViewModel.getName());
//        }
//        return rootView;
//    }
//
//    public void logout() {
//        preferencesManager.removeToken();
//        Intent intent = new Intent(this.requireContext(), MainActivity.class);
//        startActivity(intent);
//        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        requireActivity().finish();
//    }
//
//    public void loadCurrentUser() {
//        if (profileViewModel.isAwaitingResponse()) {
//            return;
//        }
//
//        profileViewModel.setAwaitingResponse(true);
//
//        authRepository.getCurrentUser(new AuthCallback<User>() {
//            @Override
//            public void onSuccess(User user) {
//                profileViewModel.setAwaitingResponse(false);
//                profileViewModel.setId(user.getId());
//                profileViewModel.setName(user.getName());
//                profileViewModel.setEmail(user.getEmail());
//                // todo add liked and disliked amount
//                showUser(user.getName());
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                profileViewModel.setAwaitingResponse(false);
//                ToastManager.showToast(requireContext(), errorMessage);
//            }
//        });
//    }
//
//    public void showUser(String name) {
//        userNameTextView.setText(name);
//
//        // todo finish
////        likeCountTextView.setText(String.valueOf(currentUser.getLikes()));
////        dislikeCountTextView.setText(String.valueOf(currentUser.getDislikes()));
//    }
//}





package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.github.sviatoslavslysh.vacationvibes.activity.ChangePasswordActivity;
import com.github.sviatoslavslysh.vacationvibes.model.ProfileViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.User;
import com.github.sviatoslavslysh.vacationvibes.repository.AuthRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.AuthCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;

public class ProfileFragment extends Fragment {
    private PreferencesManager preferencesManager;
    private AuthRepository authRepository;
    private ProfileViewModel profileViewModel;

    private TextView userNameTextView;
    private TextView likeCountTextView;
    private TextView dislikeCountTextView;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        userNameTextView = rootView.findViewById(R.id.user_name);
        likeCountTextView = rootView.findViewById(R.id.like_count);
        dislikeCountTextView = rootView.findViewById(R.id.dislike_count);

        preferencesManager = new PreferencesManager(requireContext());
        authRepository = new AuthRepository(requireContext());
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        Button logoutButton = rootView.findViewById(R.id.button_log_out);
        Button settingsButton = rootView.findViewById(R.id.button_settings);
        Button changePasswordButton = rootView.findViewById(R.id.button_change_password);

        settingsButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .commit();
        });

        logoutButton.setOnClickListener(v -> logout());

        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        if (profileViewModel.getId() == null) {
            loadCurrentUser();
        } else {
            showUser(profileViewModel.getName());
        }

        return rootView;
    }

    public void logout() {
        preferencesManager.removeToken();
        Intent intent = new Intent(requireContext(), MainActivity.class);
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
                profileViewModel.setId(user.getId());
                profileViewModel.setName(user.getName());
                profileViewModel.setEmail(user.getEmail());
                showUser(user.getName());
            }

            @Override
            public void onError(String errorMessage) {
                profileViewModel.setAwaitingResponse(false);
                ToastManager.showToast(requireContext(), errorMessage);
            }
        });
    }

    public void showUser(String name) {
        userNameTextView.setText(name);
        // todo finish
        // likeCountTextView.setText(String.valueOf(currentUser.getLikes()));
        // dislikeCountTextView.setText(String.valueOf(currentUser.getDislikes()));
    }
}
