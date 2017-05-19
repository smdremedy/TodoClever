package com.soldiersofmobile.todoekspert;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.soldiersofmobile.todoekspert.api.ErrorResponse;
import com.soldiersofmobile.todoekspert.api.TodoApi;
import com.soldiersofmobile.todoekspert.db.DbHelper;
import com.soldiersofmobile.todoekspert.db.TodoDao;
import com.soldiersofmobile.todoekspert.login.LoginPresenter;
import com.soldiersofmobile.todoekspert.login.SharedPreferencesUserStorage;
import com.soldiersofmobile.todoekspert.login.UserStorage;
import com.squareup.leakcanary.LeakCanary;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private TodoDao todoDao;

    public TodoDao getTodoDao() {
        return todoDao;
    }

    public LoginPresenter getLoginPresenter() {
        return loginPresenter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);

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

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("X-Parse-Application-Id",
                                        "X7HiVehVO7Zg9ufo0qCDXVPI3z0bFpUXtyq2ezYL")
                                .build();
                        Response response = chain.proceed(request);
                        return response;
                    }
                })
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        todoDao = new TodoDao(new DbHelper(this));
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
