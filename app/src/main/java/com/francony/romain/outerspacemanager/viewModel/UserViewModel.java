package com.francony.romain.outerspacemanager.viewModel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.AttackActivity;
import com.francony.romain.outerspacemanager.model.UserScore;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

public class UserViewModel {
    private UserScore userScore;
    private View view;
    private Context context;

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    public UserViewModel(UserScore userScore, View view, Context context) {
        this.userScore = userScore;
        this.view = view;
        this.context = context;

    }

    public UserScore getUserScore() {
        return userScore;
    }

    public void setUserScore(UserScore userScore) {
        this.userScore = userScore;
    }

    public void startAttackActivity() {
        Intent attackIntent = new Intent(context, AttackActivity.class);
        context.startActivity(attackIntent);
    }


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
}
