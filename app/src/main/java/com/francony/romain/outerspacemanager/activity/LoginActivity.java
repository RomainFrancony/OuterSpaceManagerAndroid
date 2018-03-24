package com.francony.romain.outerspacemanager.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.User;
import com.francony.romain.outerspacemanager.response.UserResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private Button btnLogin;
    private Button btnSignInLayout;
    private Button btnSignIn;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etUsername;
    private TextView tvCredential;
    private TextInputLayout tiEmail;
    private TextInputLayout tiUsername;
    private TextInputLayout tiPassword;
    private RelativeLayout laLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.etEmail = findViewById(R.id.email_input);
        this.etUsername = findViewById(R.id.username_input);
        this.etPassword = findViewById(R.id.password_input);
        this.tvCredential = findViewById(R.id.credential_error_message);
        this.tiEmail = findViewById(R.id.email_input_container);
        this.tiUsername = findViewById(R.id.username_input_container);
        this.tiPassword = findViewById(R.id.password_input_container);
        this.laLoader = findViewById(R.id.layout_loader);

        // Login button
        this.btnLogin = findViewById(R.id.login_button);
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.login();
            }
        });

        // Sign in layout
        this.btnSignInLayout = findViewById(R.id.sign_in_button_layout);
        this.btnSignInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.showSignInLayout();
            }
        });

        // Sign in button
        this.btnSignIn = findViewById(R.id.sign_in_button);
        this.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.signIn();
            }
        });
    }

    private void showSignInLayout() {
        this.tiEmail.setVisibility(View.VISIBLE);
        this.btnLogin.setVisibility(View.GONE);
        this.btnSignInLayout.setVisibility(View.GONE);
        this.btnSignIn.setVisibility(View.VISIBLE);
    }

    private void login() {
        this.tiPassword.setError(null);
        this.tiUsername.setError(null);
        User user = new User(this.etUsername.getText().toString(), this.etPassword.getText().toString());

        if (user.getUsername().trim().equals("")) {
            this.tiUsername.setError(getResources().getString(R.string.error_field_required));
            return;
        }
        if (user.getPassword().trim().equals("")) {
            this.tiPassword.setError(getResources().getString(R.string.error_field_required));
            return;
        }
        this.laLoader.setVisibility(View.VISIBLE);

        Call<UserResponse> request = this.service.userLogin(user);

        request.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                LoginActivity.this.laLoader.setVisibility(View.GONE);
                // Error
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                LoginActivity.this.launchApp(response);
            }

            // Network error
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                LoginActivity.this.laLoader.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signIn() {
        this.tiPassword.setError(null);
        this.tiUsername.setError(null);
        this.tiEmail.setError(null);
        User user = new User(this.etUsername.getText().toString(), this.etPassword.getText().toString(), this.etEmail.getText().toString());

        if (user.getUsername().trim().equals("")) {
            this.tiUsername.setError(getResources().getString(R.string.error_field_required));
            return;
        }
        if (user.getPassword().trim().equals("")) {
            this.tiPassword.setError(getResources().getString(R.string.error_field_required));
            return;
        }
        if (user.getEmail().trim().equals("")) {
            this.tiEmail.setError(getResources().getString(R.string.error_field_required));
            return;
        }
        this.laLoader.setVisibility(View.VISIBLE);

        Call<UserResponse> request = this.service.userSignIn(user);

        request.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                LoginActivity.this.laLoader.setVisibility(View.GONE);
                // Error
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                LoginActivity.this.launchApp(response);
            }

            // Network error
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                LoginActivity.this.laLoader.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void launchApp(Response<UserResponse> response){
        SharedPreferencesHelper.setToken(getApplicationContext(), response.body().getToken());
        SharedPreferencesHelper.setTokenexpiration(getApplicationContext(), response.body().getExpires());

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

}
