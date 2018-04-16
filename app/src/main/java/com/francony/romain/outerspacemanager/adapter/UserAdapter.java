package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterUserBinding;
import com.francony.romain.outerspacemanager.model.UserScore;
import com.francony.romain.outerspacemanager.viewModel.UserViewModel;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter implements View.OnScrollChangeListener {
    private ArrayList<UserScore> usersDataset;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView recyclerView;

    private static final int TYPE_USER = 0;
    private static final int TYPE_LOADING = 1;

    public UserAdapter(ArrayList<UserScore> userScores, Context context) {
        this.usersDataset = userScores;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        this.recyclerView.setOnScrollChangeListener(this);
    }

    @Override
    public int getItemViewType(int position) {
        return this.usersDataset.get(position) == null ? TYPE_LOADING : TYPE_USER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            AdapterUserBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_user, parent, false);
            return new UserAdapterViewHolder(binding);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_loading, parent, false);
        return new UserAdapterLoadingViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserAdapterViewHolder) {
            UserScore userScore = this.usersDataset.get(position);
            userScore.setPosition(position);
            ((UserAdapterViewHolder) holder).bind(userScore, this.context);
        }
    }

    @Override
    public int getItemCount() {
        return usersDataset.size();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (((LinearLayoutManager) this.recyclerView.getLayoutManager()).findLastVisibleItemPosition() == this.getItemCount() - 1) {
            if (this.onLoadMoreListener != null) {
                this.onLoadMoreListener.onLoadMore();
            }
        }
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


    public class UserAdapterLoadingViewHolder extends RecyclerView.ViewHolder {

        public UserAdapterLoadingViewHolder(View v) {
            super(v);
        }

        public void bind() {
        }
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
