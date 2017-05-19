package com.soldiersofmobile.todoekspert.login;

import android.os.AsyncTask;
import android.util.Log;

import com.soldiersofmobile.todoekspert.api.ErrorResponse;
import com.soldiersofmobile.todoekspert.api.TodoApi;
import com.soldiersofmobile.todoekspert.api.User;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginPresenter {

    private LoginView loginView;

    private boolean showProgress;
    private String error = null;

    private UserStorage userStorage;
    private final TodoApi todoApi;
    private final Converter<ResponseBody, ErrorResponse> converter;

    public LoginPresenter(UserStorage userStorage, TodoApi todoApi,
                          Converter<ResponseBody, ErrorResponse> converter) {
        this.userStorage = userStorage;
        this.todoApi = todoApi;
        this.converter = converter;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
        refreshProgress();
        refreshResult();
    }

    public void login(final String username, String password) {



        final Call<User> call = todoApi.getLogin(username, password);

        AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress = true;
                error = null;
                refreshProgress();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    Response<User> response = call.execute();
                    if (response.isSuccessful()) {
                        User user = response.body();
                        Log.d("TAG", user.toString());

                        userStorage.storeUser(user);
                        return null;
                    } else {
                        ErrorResponse errorResponse
                                = converter.convert(response.errorBody());
                        return errorResponse.error;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                showProgress = false;
                error = result;


                refreshProgress();
                refreshResult();

            }
        };

        asyncTask.execute(username, password);
    }

    private void refreshProgress() {
        if (loginView != null) {
            loginView.showProgress(showProgress);
        }
    }

    private void refreshResult() {
        if (loginView != null) {
            if (error != null) {
                loginView.onError(error);
            } else if (userStorage.isLoggedIn()) {
                loginView.onSuccess();
            }
        }
    }
}
