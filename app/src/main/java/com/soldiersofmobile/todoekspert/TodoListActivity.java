package com.soldiersofmobile.todoekspert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.soldiersofmobile.todoekspert.api.ErrorResponse;
import com.soldiersofmobile.todoekspert.api.Todo;
import com.soldiersofmobile.todoekspert.api.TodoApi;
import com.soldiersofmobile.todoekspert.api.TodosResponse;
import com.soldiersofmobile.todoekspert.login.LoginActivity;
import com.soldiersofmobile.todoekspert.login.UserStorage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import timber.log.Timber;

import static timber.log.Timber.*;

public class TodoListActivity extends AppCompatActivity {

    private UserStorage userStorage;
    private TodoApi todoApi;
    private Converter<ResponseBody, ErrorResponse> converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App application = (App) getApplication();
        userStorage = application.getUserStorage();
        todoApi = application.getTodoApi();
        converter = application.getConverter();

        if (!userStorage.isLoggedIn()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_todo_list);

    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                break;
            case R.id.action_refresh:
                refresh();
                break;
            case R.id.action_logout:
                userStorage.logout();
                goToLogin();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void refresh() {
        Call<TodosResponse> call = todoApi.getTodos(userStorage.getToken());
        call.enqueue(new Callback<TodosResponse>() {
            @Override
            public void onResponse(Call<TodosResponse> call, Response<TodosResponse> response) {

                if (response.isSuccessful()) {
                    TodosResponse body = response.body();
                    for (Todo todo : body.results) {
                        d(todo.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<TodosResponse> call, Throwable t) {
                e(t);

            }
        });
    }
}
