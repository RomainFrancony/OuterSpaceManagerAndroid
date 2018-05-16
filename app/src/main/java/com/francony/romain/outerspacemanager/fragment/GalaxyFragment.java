package com.francony.romain.outerspacemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.adapter.UserAdapter;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.UserScore;
import com.francony.romain.outerspacemanager.response.ScoreboardResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GalaxyFragment extends Fragment implements UserAdapter.OnLoadMoreListener {
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private RelativeLayout laLoader;
    private RecyclerView rvUsers;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<UserScore> userScores = new ArrayList<>();
    private UserAdapter userAdapter;
    private Boolean loading = true;
    private int page = 0;


    public GalaxyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_galaxy, container, false);

        this.laLoader = v.findViewById(R.id.layout_loader);

        // Recycler view
        this.rvUsers = v.findViewById(R.id.users_rv);
        this.rvUsers.setHasFixedSize(true);
        this.rvLayoutManager = new LinearLayoutManager(getContext());
        this.rvUsers.setLayoutManager(rvLayoutManager);
        this.userAdapter = new UserAdapter(this.userScores, getContext());
        this.rvUsers.setAdapter(this.userAdapter);
        this.userAdapter.setOnLoadMoreListener(this);
        this.getScoreboard();

        return v;
    }

    /**
     * API Call
     */
    private void getScoreboard() {
        this.loading = true;
        Call<ScoreboardResponse> request = this.service.userList(SharedPreferencesHelper.getToken(getContext()), this.page * 20, 20);

        request.enqueue(new Callback<ScoreboardResponse>() {

            @Override
            public void onResponse(Call<ScoreboardResponse> call, Response<ScoreboardResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    GalaxyFragment.this.loading = false;
                    Toast.makeText(getContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }


                // Delete loading item
                if (!GalaxyFragment.this.userScores.isEmpty()) {
                    int index = GalaxyFragment.this.userScores.size() - 1;
                    GalaxyFragment.this.userScores.remove(index);
                    GalaxyFragment.this.userAdapter.notifyItemRemoved(index);
                }

                // We are already on last page no need to do anything
                if (response.body().getSize() == 0) {
                    return;
                }

                // Add items to data set and hide loader
                int initialSize =  GalaxyFragment.this.userScores.size();
                GalaxyFragment.this.userScores.addAll(response.body().getUsers());
                GalaxyFragment.this.userScores.add(null);
                GalaxyFragment.this.rvUsers.setVisibility(View.VISIBLE);
                GalaxyFragment.this.laLoader.setVisibility(View.GONE);
                GalaxyFragment.this.userAdapter.notifyItemRangeInserted(initialSize,GalaxyFragment.this.userScores.size());
                GalaxyFragment.this.loading = false;
            }

            // Network error
            @Override
            public void onFailure(Call<ScoreboardResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Load next page
     */
    @Override
    public void onLoadMore() {
        if (this.loading) {
            return;
        }
        this.page++;
        this.getScoreboard();
    }
}
