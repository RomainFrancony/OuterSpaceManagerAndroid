package com.francony.romain.outerspacemanager.viewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.francony.romain.outerspacemanager.BR;
import com.francony.romain.outerspacemanager.HasUserInfo;
import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.UserInfoManager;
import com.francony.romain.outerspacemanager.activity.AttackActivity;
import com.francony.romain.outerspacemanager.model.UserScore;
import com.francony.romain.outerspacemanager.response.UserInfoResponse;
import com.google.gson.Gson;

public class UserViewModel extends BaseObservable implements HasUserInfo {
    private UserScore userScore;
    private View view;
    private Context context;
    private UserInfoResponse currentUser;


    public UserViewModel(UserScore userScore, View view, Context context) {
        this.userScore = userScore;
        this.view = view;
        this.context = context;
        UserInfoManager.getInstance().addOnUserInfoUpdateListener(this);
    }

    /**
     * Check if user is the one connected
     *
     * @return
     */
    @Bindable
    public UserInfoResponse getCurrentUser() {
        if (currentUser == null) {
            return null;
        }
        return currentUser;
    }

    /**
     * Start activity
     */
    public void startAttackActivity() {
        Intent attackIntent = new Intent(context, AttackActivity.class);
        Gson gson = new Gson();
        String user_json = gson.toJson(this.userScore);
        attackIntent.putExtra("user", user_json);
        context.startActivity(attackIntent);
    }


    /**
     * Change color if in top 3
     *
     * @return
     */
    public int getUserColor() {
        if (this.userScore.getPosition() == 0) {
            return context.getResources().getColor(R.color.colorGold, context.getTheme());
        }

        if (this.userScore.getPosition() == 1) {
            return context.getResources().getColor(R.color.colorSilver, context.getTheme());
        }

        if (this.userScore.getPosition() == 2) {
            return context.getResources().getColor(R.color.colorBronze, context.getTheme());
        }

        return context.getResources().getColor(R.color.colorGray, context.getTheme());
    }

    /**
     * Listen to current user updates
     *
     * @param info
     */
    @Override
    public void OnUserInfoUpdate(UserInfoResponse info) {
        this.currentUser = info;
        notifyPropertyChanged(BR.currentUser);
    }

    public UserScore getUserScore() {
        return userScore;
    }

    public void setUserScore(UserScore userScore) {
        this.userScore = userScore;
    }
}
