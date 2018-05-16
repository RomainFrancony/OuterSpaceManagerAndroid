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
import com.francony.romain.outerspacemanager.adapter.SearchAdapter;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Search;
import com.francony.romain.outerspacemanager.response.SearchesResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchesFragment extends Fragment {

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private RelativeLayout laLoader;
    private RecyclerView rvSearches;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<Search> searches = new ArrayList<>();
    private SearchAdapter searchAdapter;


    public SearchesFragment() {
        // Required empty public constructor
    }

    /**
     * Remove adapter so the view models stop their handler for refreshing the UI
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvSearches.setAdapter(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_searches, container, false);


        this.laLoader = v.findViewById(R.id.layout_loader);

        // Recycler view
        this.rvSearches = v.findViewById(R.id.searches_rv);
        this.rvSearches.setHasFixedSize(true);
        this.rvLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        this.rvSearches.setLayoutManager(rvLayoutManager);
        this.searchAdapter = new SearchAdapter(this.searches, getContext());
        this.rvSearches.setAdapter(this.searchAdapter);
        this.getSearches();
        return v;
    }

    /**
     * API call
     */
    public void getSearches() {
        Call<SearchesResponse> request = this.service.searchList(SharedPreferencesHelper.getToken(getActivity().getApplicationContext()));

        request.enqueue(new Callback<SearchesResponse>() {

            @Override
            public void onResponse(Call<SearchesResponse> call, Response<SearchesResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                SearchesFragment.this.searches.addAll(response.body().getSearches());
                SearchesFragment.this.laLoader.setVisibility(View.GONE);
                SearchesFragment.this.rvSearches.setVisibility(View.VISIBLE);
                SearchesFragment.this.searchAdapter.notifyDataSetChanged();
            }

            // Network error
            @Override
            public void onFailure(Call<SearchesResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }
}
