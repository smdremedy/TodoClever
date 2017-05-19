package com.soldiersofmobile.todoekspert.login;

import com.soldiersofmobile.todoekspert.api.User;

/**
 * Created by madejs on 18.05.17.
 */

public interface UserStorage {

    void storeUser(User user);
    boolean isLoggedIn();

    void logout();

    String getToken();
}
