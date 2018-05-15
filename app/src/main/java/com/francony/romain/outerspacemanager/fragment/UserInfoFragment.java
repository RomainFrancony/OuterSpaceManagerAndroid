package com.francony.romain.outerspacemanager.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.FragmentUserInfoBinding;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.response.UserInfoResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoFragment extends Fragment {

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();
    // Keep context as variable because we want to save user info even after the user may have quit the fragment (and we need the context to save it in shared preferences)
    private Context context;

    private FragmentUserInfoBinding binding;
    private Handler handler;

    public UserInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false);

        this.binding.setLoadingState(true);
        this.initRefreshInfo();
        return binding.getRoot();
    }

    private void initRefreshInfo() {
        this.handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (UserInfoFragment.this.getView() == null || !UserInfoFragment.this.getView().isShown()) {
                    return;
                }

                UserInfoFragment.this.getUserInfos();
                UserInfoFragment.this.handler.postDelayed(this, 5000);
            }
        }, 0);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void getUserInfos() {
        Call<UserInfoResponse> request = this.service.userInfo(SharedPreferencesHelper.getToken(context));
        request.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferencesHelper.setUserInfos(context, response.body());

                // Transition
                UserInfoFragment.this.binding.setLoadingState(false);
                UserInfoFragment.this.binding.setUser(response.body());
            }

            // Network error
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
            }
        });
    }
}
