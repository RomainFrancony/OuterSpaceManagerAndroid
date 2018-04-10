package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterUserBinding;
import com.francony.romain.outerspacemanager.model.UserScore;
import com.francony.romain.outerspacemanager.viewModel.UserViewModel;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> {
    private ArrayList<UserScore> usersDataset;
    private Context context;

    public UserAdapter(ArrayList<UserScore> userScores, Context context) {
        this.usersDataset = userScores;
        this.context = context;
    }


    @Override
    public UserAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterUserBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_user, parent, false);
        return new UserAdapterViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(UserAdapterViewHolder holder, int position) {
        UserScore userScore = this.usersDataset.get(position);
        userScore.setPosition(position);
        holder.bind(userScore, this.context);
    }

    @Override
    public int getItemCount() {
        return usersDataset.size();
    }


    public class UserAdapterViewHolder extends RecyclerView.ViewHolder {
        private final AdapterUserBinding binding;

        public UserAdapterViewHolder(AdapterUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final UserScore userScore, Context context) {
            UserViewModel userViewModel = new UserViewModel(userScore, binding.getRoot(), context);
            binding.setUserViewModel(userViewModel);
            binding.executePendingBindings();
        }
    }
}
