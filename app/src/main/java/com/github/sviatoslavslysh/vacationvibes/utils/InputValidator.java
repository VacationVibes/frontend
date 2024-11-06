package com.github.sviatoslavslysh.vacationvibes.utils;

public class InputValidator {
    public InputValidator() {

    }

    public boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 6 && password.length() <= 511;
    }

    public boolean isValidName(String name) {
        return name.length() >= 2 && name.length() <= 127;
    }

}
