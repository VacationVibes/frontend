package com.github.sviatoslavslysh.vacationvibes.functionality;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.functionality.fragment.HistoryFragment;
import com.github.sviatoslavslysh.vacationvibes.functionality.fragment.HomeFragment;
import com.github.sviatoslavslysh.vacationvibes.functionality.fragment.ProfileFragment;
import com.github.sviatoslavslysh.vacationvibes.model.HistoryViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.HomeViewModel;
import com.github.sviatoslavslysh.vacationvibes.repository.PlaceRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuyakaido.android.cardstackview.CardStackView;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class NavigationBarActivity extends AppCompatActivity {
    private PlaceRepository placeRepository;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);

        placeRepository = new PlaceRepository(this);
        preferencesManager = new PreferencesManager(this);
        new ViewModelProvider(this).get(HomeViewModel.class);
        new ViewModelProvider(this).get(HistoryViewModel.class);

        BottomNavigationView navView = findViewById(R.id.navbar);
        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.navigation_history) {
                selectedFragment = new HistoryFragment();
            }
            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }

    public PlaceRepository getPlaceRepository() {
        return placeRepository;
    }

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

}
