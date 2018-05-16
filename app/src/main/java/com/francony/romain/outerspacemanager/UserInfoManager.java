package com.francony.romain.outerspacemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.activity.LoginActivity;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.response.UserInfoResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoManager {
    private Context context;
    private ArrayList<HasUserInfo> listeners;
    private Handler handler;
    private UserInfoResponse userInfo;
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private static UserInfoManager instance;

    public static UserInfoManager getInstance() {
        if (instance == null) instance = getSync();
        return instance;
    }

    private static synchronized UserInfoManager getSync() {
        if (instance == null) instance = new UserInfoManager();
        return instance;
    }

    private UserInfoManager() {
        this.listeners = new ArrayList<>();
        this.context = OuterSpaceManager.getInstance();
        this.initHandler();
    }


    private void initHandler() {
        this.handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                UserInfoManager.this.getUserInfos();
                UserInfoManager.this.handler.postDelayed(this, 5000);
            }
        }, 0);
    }

    private void getUserInfos() {
        Call<UserInfoResponse> request = this.service.userInfo(SharedPreferencesHelper.getToken(context));
        request.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                // Error
                if (!response.isSuccessful()) {

                    if (response.code() == 403) {
                        UserInfoManager.this.askCrendentialsForRelog();
                    }

                    Toast.makeText(context, Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                UserInfoManager.this.userInfo = response.body();
                // Trigger all listeners
                // TODO : none of them is removed for the moment event when it's not needed
                for (HasUserInfo item : listeners) {
                    item.OnUserInfoUpdate(UserInfoManager.this.userInfo);
                }
            }

            // Network error
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
            }
        });
    }

    private void askCrendentialsForRelog() {
        FlowManager.getDatabase(OuterSpaceManagerDatabase.class).reset();
        SharedPreferencesHelper.clearToken(this.context);
        SharedPreferencesHelper.clearTokenExpiration(this.context);
        Intent loginIntent = new Intent(this.context, LoginActivity.class);
        this.context.startActivity(loginIntent);
    }

    public void addOnUserInfoUpdateListener(HasUserInfo listener) {
        // Trigger the listener directly if we have the information
        if (this.userInfo != null) {
            listener.OnUserInfoUpdate(this.userInfo);
        }
        this.listeners.add(listener);
    }
}
