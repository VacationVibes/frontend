package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.sviatoslavslysh.vacationvibes.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SettingsFragment extends Fragment {
    private TextView themeTextView;
    private RadioGroup themeRadioGroup;
    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        themeTextView = rootView.findViewById(R.id.themeTextView);
        themeRadioGroup = rootView.findViewById(R.id.themeRadioGroup);



    }
}
