package com.soldiersofmobile.todoekspert.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.soldiersofmobile.todoekspert.App;
import com.soldiersofmobile.todoekspert.R;
import com.soldiersofmobile.todoekspert.todolist.TodoListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.username)
    TextInputEditText usernameEditText;
    @BindView(R.id.password)
    TextInputEditText passwordEditText;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.usernameLayout)
    TextInputLayout usernameLayout;
    @BindView(R.id.progress)
    ProgressBar progress;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter = ((App) getApplication()).getLoginPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setLoginView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.setLoginView(null);
    }

    @OnClick(R.id.login)
    public void onLoginClicked() {

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValid = true;
        if (username.isEmpty()) {
            isValid = false;
            usernameLayout.setError(getString(R.string.username_error));
        }
        if (password.isEmpty()) {
            isValid = false;
            passwordEditText.setError(getString(R.string.password_error));
        }

        if (isValid) {
            presenter.login(username, password);
        }

    }


    @OnClick(R.id.register)
    public void onRegisterClicked() {
    }

    @Override
    public void showProgress(boolean show) {
        login.setEnabled(!show);
    }

    @Override
    public void onSuccess() {
        finish();
        Intent intent = new Intent(this, TodoListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
