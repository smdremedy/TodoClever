package com.soldiersofmobile.todoekspert.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.soldiersofmobile.todoekspert.App;
import com.soldiersofmobile.todoekspert.R;
import com.soldiersofmobile.todoekspert.RefreshIntentService;
import com.soldiersofmobile.todoekspert.api.ErrorResponse;
import com.soldiersofmobile.todoekspert.api.Todo;
import com.soldiersofmobile.todoekspert.api.TodoApi;
import com.soldiersofmobile.todoekspert.api.TodosResponse;
import com.soldiersofmobile.todoekspert.db.DbHelper;
import com.soldiersofmobile.todoekspert.db.TodoDao;
import com.soldiersofmobile.todoekspert.login.LoginActivity;
import com.soldiersofmobile.todoekspert.login.UserStorage;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

import static timber.log.Timber.d;
import static timber.log.Timber.e;

public class TodoListActivity extends AppCompatActivity {

    private static final String[] FROM = {
            TodoDao.C_CONTENT, TodoDao.C_DONE, TodoDao.C_ID
    };
    private static final int[] TO = {
            R.id.item_checkbox, R.id.item_checkbox, R.id.item_button
    };
    @BindView(R.id.list)
    ListView list;
    private UserStorage userStorage;
    private TodoApi todoApi;
    private Converter<ResponseBody, ErrorResponse> converter;
    private TodoAdapter adapter;
    private SimpleCursorAdapter cursorAdapter;
    private TodoDao todoDao;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Cursor cursor = todoDao.getTodosByUser(userStorage.getUserId());
            cursorAdapter.swapCursor(cursor);
            Toast.makeText(context, "Refreshed!", Toast.LENGTH_SHORT).show();


        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(RefreshIntentService.REFRESH_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App application = (App) getApplication();
        userStorage = application.getUserStorage();
        todoApi = application.getTodoApi();
        converter = application.getConverter();
        todoDao = application.getTodoDao();

        if (!userStorage.isLoggedIn()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);


        adapter = new TodoAdapter();
        Cursor cursor = todoDao.getTodosByUser(userStorage.getUserId());

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_todo, cursor,
                FROM, TO, 0);
        cursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(TodoDao.C_DONE)) {
                    boolean done = cursor.getInt(columnIndex) > 0;
                    CheckBox checkBox = (CheckBox) view;
                    checkBox.setChecked(done);
                    return true;
                }
                return false;
            }
        });

        list.setAdapter(cursorAdapter);

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

        Intent intent = new Intent(this, RefreshIntentService.class);
        startService(intent);


    }
}
