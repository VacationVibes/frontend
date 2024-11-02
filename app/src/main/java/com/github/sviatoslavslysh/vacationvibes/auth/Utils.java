package com.github.sviatoslavslysh.vacationvibes.auth;

public class Utils {
    public Utils() {

    }

    public boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}
