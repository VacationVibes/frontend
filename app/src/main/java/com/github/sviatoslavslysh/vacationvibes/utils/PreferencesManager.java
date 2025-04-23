package com.github.sviatoslavslysh.vacationvibes.utils;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.sviatoslavslysh.vacationvibes.R;

public class PreferencesManager {

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_TOKEN = "token";
    private static final String FIRST_OPEN = "first_open";
    private static final String THEME = "theme";
    private static final String BUTTONSELECTED = "button";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void removeToken() {
        editor.remove(KEY_TOKEN);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public boolean isFirstOpen() {
        return sharedPreferences.getBoolean(FIRST_OPEN, true);
    }

    public void setFirstOpen(boolean firstOpen) {
        editor.putBoolean(FIRST_OPEN, firstOpen);
        editor.apply();
    }

    public void setTheme(String theme) {
        editor.putString(THEME, "system");
        editor.apply();
    }

    public void setButtonSelected(int ButtonSelected) {
        editor.putInt(BUTTONSELECTED, ButtonSelected);
        editor.apply();
    }

    public int getButtonSelected() {
        return sharedPreferences.getInt(BUTTONSELECTED, R.id.SystemButton);
    }
    public String getTheme() {
        return sharedPreferences.getString(THEME, "System");
    }
}
