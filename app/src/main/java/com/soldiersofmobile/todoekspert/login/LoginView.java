package com.soldiersofmobile.todoekspert.login;

/**
 * Created by madejs on 18.05.17.
 */

public interface LoginView {

    void showProgress(boolean show);

    void onSuccess();

    void onError(String error);

}
