package com.soldiersofmobile.todoekspert;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.soldiersofmobile.todoekspert.api.ErrorResponse;
import com.soldiersofmobile.todoekspert.api.TodoApi;
import com.soldiersofmobile.todoekspert.login.LoginPresenter;
import com.soldiersofmobile.todoekspert.login.SharedPreferencesUserStorage;
import com.soldiersofmobile.todoekspert.login.UserStorage;
import com.squareup.leakcanary.LeakCanary;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class App extends Application {

    private LoginPresenter loginPresenter;
    private UserStorage userStorage;
    private TodoApi todoApi;
    private Converter<ResponseBody, ErrorResponse> converter;

    public LoginPresenter getLoginPresenter() {
        return loginPresenter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {

                }
            });
        }

        SharedPreferences preferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        userStorage = new SharedPreferencesUserStorage(preferences);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        todoApi = retrofit.create(TodoApi.class);
        converter = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[]{});


        loginPresenter = new LoginPresenter(userStorage, todoApi, converter);
    }

    public TodoApi getTodoApi() {
        return todoApi;
    }

    public Converter<ResponseBody, ErrorResponse> getConverter() {
        return converter;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
