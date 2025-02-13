package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.model.ProfileViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.SettingsViewModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SettingsFragment extends Fragment {
    private TextView themeTextView;
    private RadioGroup themeRadioGroup;
    private RadioButton LightButton;
    private RadioButton DarkButton;
    private RadioButton SystemButton;
    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        themeTextView = rootView.findViewById(R.id.themeTextView);
        themeRadioGroup = rootView.findViewById(R.id.themeRadioGroup);
        LightButton = rootView.findViewById(R.id.LightButton);
        DarkButton = rootView.findViewById(R.id.DarkButton);
        SystemButton = rootView.findViewById(R.id.SystemButton);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        LightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        DarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        SystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        });

        return rootView;
    }

    public void LightSelect() {

    }

}
