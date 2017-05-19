package com.soldiersofmobile.todoekspert;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.soldiersofmobile.todoekspert.api.Todo;
import com.soldiersofmobile.todoekspert.api.TodoApi;
import com.soldiersofmobile.todoekspert.api.TodosResponse;
import com.soldiersofmobile.todoekspert.db.TodoDao;
import com.soldiersofmobile.todoekspert.login.UserStorage;
import com.soldiersofmobile.todoekspert.todolist.TodoListActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * Created by madejs on 19.05.17.
 */

public class RefreshIntentService extends IntentService {

    public static final String REFRESH_ACTION = BuildConfig.APPLICATION_ID + ".REFRESH";

    public RefreshIntentService() {
        super(RefreshIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("Refresh");

        App app = (App) getApplication();
        TodoApi todoApi = app.getTodoApi();
        TodoDao todoDao = app.getTodoDao();
        UserStorage userStorage = app.getUserStorage();


        Call<TodosResponse> call = todoApi.getTodos(userStorage.getToken());
        try {
            Response<TodosResponse> response = call.execute();
            if (response.isSuccessful()) {
                TodosResponse body = response.body();
                for (Todo todo : body.results) {
                    d(todo.toString());
                    todoDao.insert(todo, userStorage.getUserId());
                }

                Intent broadcast = new Intent(REFRESH_ACTION);
                sendBroadcast(broadcast);

                NotificationManager notificationManager
                        = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentTitle("Todos refreshed");
                builder.setContentText("Added:" + body.results.length);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setAutoCancel(true);

                Intent activityIntent = new Intent(this, TodoListActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                PendingIntent pendingIntent
                        = PendingIntent.getActivity(this, 1, activityIntent, 0);
                builder.setContentIntent(pendingIntent);

                notificationManager.notify(1, builder.build());
            }
        } catch (IOException e) {
            e(e);
        }

    }
}
