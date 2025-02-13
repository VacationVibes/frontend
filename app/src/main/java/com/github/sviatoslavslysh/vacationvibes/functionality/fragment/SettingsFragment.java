package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.os.Bundle;
import android.os.SharedMemory;
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
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.model.ProfileViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.SettingsViewModel;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SettingsFragment extends Fragment {
    private TextView themeTextView;
    private RadioGroup themeRadioGroup;
    private RadioButton LightButton;
    private RadioButton DarkButton;
    private RadioButton SystemButton;
    private SettingsViewModel settingsViewModel;
    private PreferencesManager preferencesManager;

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        themeTextView = rootView.findViewById(R.id.themeTextView);
        themeRadioGroup = rootView.findViewById(R.id.themeRadioGroup);
        preferencesManager = ((NavigationBarActivity) requireActivity()).getPreferencesManager();
        LightButton = rootView.findViewById(R.id.LightButton);
        DarkButton = rootView.findViewById(R.id.DarkButton);
        SystemButton = rootView.findViewById(R.id.SystemButton);
        themeRadioGroup.check(preferencesManager.getButtonSelected());

        LightButton.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            preferencesManager.setTheme("light");
            preferencesManager.setButtonSelected(LightButton.getId());
        });

        DarkButton.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            preferencesManager.setTheme("dark");
            preferencesManager.setButtonSelected(DarkButton.getId());
        });

        SystemButton.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            preferencesManager.setTheme("system");
            preferencesManager.setButtonSelected(SystemButton.getId());
        });
        return rootView;
    }
}
