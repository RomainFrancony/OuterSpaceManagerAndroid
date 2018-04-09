package com.francony.romain.outerspacemanager.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoFragment extends Fragment {

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private FragmentUserInfoBinding binding;

    public UserInfoFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info,container,false);

        this.binding.setLoadingState(true);
        this.getUserInfos();
        return binding.getRoot();
    }

    private void getUserInfos() {
        Call<UserInfoResponse> request = this.service.userInfo(SharedPreferencesHelper.getToken(getContext()));
        request.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferencesHelper.setUserInfos(getContext(), response.body());

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
