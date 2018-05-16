package com.francony.romain.outerspacemanager.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.HasUserInfo;
import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.UserInfoManager;
import com.francony.romain.outerspacemanager.databinding.FragmentUserInfoBinding;
import com.francony.romain.outerspacemanager.response.UserInfoResponse;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoFragment extends Fragment implements HasUserInfo{
    private FragmentUserInfoBinding binding;

    public UserInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false);
        this.binding.setLoadingState(true);
        UserInfoManager.getInstance().addOnUserInfoUpdateListener(this);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void OnUserInfoUpdate(UserInfoResponse info) {
        this.binding.setLoadingState(false);
        this.binding.setUser(info);
    }
}
