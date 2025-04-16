package com.github.sviatoslavslysh.vacationvibes.utils;

import android.content.Context;
import android.widget.Toast;

import com.github.sviatoslavslysh.vacationvibes.model.User;
import com.github.sviatoslavslysh.vacationvibes.repository.AuthRepository;

import java.util.concurrent.CountDownLatch;

public class UserManager {

    private static UserManager instance;
    private User currentUser;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void loadUser(Context context) {
        if (currentUser != null) return;
        new AuthRepository(context).getCurrentUser(new AuthCallback<User>() {
            @Override
            public void onSuccess(User user) {
                setCurrentUser(user);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Failed to load user: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
