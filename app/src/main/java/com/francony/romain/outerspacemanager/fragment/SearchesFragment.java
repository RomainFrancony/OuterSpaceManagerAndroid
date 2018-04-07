package com.francony.romain.outerspacemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.adapter.SearchAdapter;
import com.francony.romain.outerspacemanager.adapter.ShipAdapter;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Search;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.response.SearchesResponse;
import com.francony.romain.outerspacemanager.response.SpaceyardResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchesFragment extends Fragment {

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private RelativeLayout laLoader;
    private RecyclerView rvShips;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<Search> searches = new ArrayList<>();
    private SearchAdapter searchAdapter = new SearchAdapter(this.searches, this);


    public SearchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_searches, container, false);


        this.laLoader = v.findViewById(R.id.layout_loader);
        this.rvShips = v.findViewById(R.id.searches_rv);
        this.rvShips.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvShips.setLayoutManager(rvLayoutManager);
        rvShips.setAdapter(this.searchAdapter);
        this.getSearches();
        return v;
    }




    public void getSearches() {
        Call<SearchesResponse> request = this.service.searchList(SharedPreferencesHelper.getToken(getActivity().getApplicationContext()));

        request.enqueue(new Callback<SearchesResponse>() {

            @Override
            public void onResponse(Call<SearchesResponse> call, Response<SearchesResponse> response) {
                // Error
                if (response.code() != 200) {
                    Toast.makeText(getActivity().getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                SearchesFragment.this.searches.addAll(response.body().getSearches());
                SearchesFragment.this.laLoader.setVisibility(View.GONE);
                SearchesFragment.this.rvShips.setVisibility(View.VISIBLE);
                SearchesFragment.this.searchAdapter.notifyDataSetChanged();
            }

            // Network error
            @Override
            public void onFailure(Call<SearchesResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }



    public void startSearch(Search search){
        Log.wtf("ah",search.getName());
    }

    public void test(){
        Log.wtf("ah","z");
    }

}
