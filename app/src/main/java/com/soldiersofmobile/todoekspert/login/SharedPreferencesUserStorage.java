package com.soldiersofmobile.todoekspert.login;

import android.content.SharedPreferences;

import com.soldiersofmobile.todoekspert.api.User;


public class SharedPreferencesUserStorage implements UserStorage {

    public static final String USER_ID = "userId";
    public static final String TOKEN = "token";
    private SharedPreferences preferences;

    public SharedPreferencesUserStorage(SharedPreferences preferences) {
        this.preferences = preferences;
    }


    @Override
    public void storeUser(User user) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, user.getObjectId());
        editor.putString(TOKEN, user.getSessionToken());
        editor.apply();


    }

    @Override
    public boolean isLoggedIn() {
        return preferences.getString(TOKEN, null) != null;
    }

    @Override
    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_ID);
        editor.remove(TOKEN);
        editor.apply();
    }

    @Override
    public String getToken() {
        return preferences.getString(TOKEN, "");
    }
}
