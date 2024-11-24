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

        placeRepository = new PlaceRepository();
        preferencesManager = new PreferencesManager(this);
        new ViewModelProvider(this).get(HomeViewModel.class);

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

        if (preferencesManager.isFirstOpen()) {
            preferencesManager.setFirstOpen(false);

            // todo the first tutorial to see must be tutorial on how to swipe

            new MaterialTapTargetPrompt.Builder(NavigationBarActivity.this)
                    .setTarget(R.id.navigation_home)
                    .setPrimaryText("Home")
                    .setSecondaryText("this is the home menu. here you can see locations we think you will like.")
                    .setBackgroundColour(ResourcesCompat.getColor(getResources(), R.color.tutorialcolor, getTheme()))
                    .setPromptStateChangeListener((prompt, state1) -> {
                        if (state1 == MaterialTapTargetPrompt.STATE_DISMISSED) {
                            new MaterialTapTargetPrompt.Builder(NavigationBarActivity.this)
                                    .setTarget(R.id.card_stack_view)
                                    .setPrimaryText("Locations")
                                    .setSecondaryText(" Swipe the card left to dislike and right to like")
                                    .setBackgroundColour(ResourcesCompat.getColor(getResources(), R.color.tutorialcolor, getTheme()))
                                    .setPromptStateChangeListener((prompt1, state2) -> {
                                        if (state2 == MaterialTapTargetPrompt.STATE_DISMISSED) {
                                            new MaterialTapTargetPrompt.Builder(NavigationBarActivity.this)
                                                    .setTarget(R.id.navigation_history)
                                                    .setPrimaryText("History")
                                                    .setSecondaryText("Accidentally swiped wrong location? You can find all of them here!")
                                                    .setBackgroundColour(ResourcesCompat.getColor(getResources(), R.color.tutorialcolor, getTheme()))
                                                    .setPromptStateChangeListener((prompt2, state3) -> {
                                                        if (state3 == MaterialTapTargetPrompt.STATE_DISMISSED) {
                                                            // todo get to the next button
                                                            // User has pressed the prompt target
                                                            new MaterialTapTargetPrompt.Builder(NavigationBarActivity.this)
                                                                    .setTarget(R.id.navigation_profile)
                                                                    .setPrimaryText("Profile")
                                                                    .setSecondaryText("This is the profile button. press it to view your profile and options like log-out")
                                                                    .setBackgroundColour(ResourcesCompat.getColor(getResources(), R.color.tutorialcolor, getTheme()))
                                                                    .show();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    })
                                    .show();
                        }
                    })
                    .show();
        }

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
