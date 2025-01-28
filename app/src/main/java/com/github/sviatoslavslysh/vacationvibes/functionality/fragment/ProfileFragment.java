package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.sviatoslavslysh.vacationvibes.MainActivity;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    private PreferencesManager preferencesManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        preferencesManager = new PreferencesManager(this.requireContext());
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button logoutButton = view.findViewById(R.id.button_log_out);
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    public void logout() {
        preferencesManager.removeToken();
        Intent intent = new Intent(this.requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        requireActivity().finish();
    }
}
