package com.francony.romain.outerspacemanager.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Search;
import com.francony.romain.outerspacemanager.BR;
import com.francony.romain.outerspacemanager.response.ActionResponse;
import com.francony.romain.outerspacemanager.response.ShipBuildingResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.hkm.ui.processbutton.iml.SubmitProcessButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends BaseObservable {
    private Search search;
    private View view;
    private Context context;

    private Boolean searchBuildLoading = false;

    private SubmitProcessButton button;

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    public SearchViewModel(Search search, View view, Context context) {
        this.search = search;
        this.view = view;
        this.context = context;


        this.button = view.findViewById(R.id.search_action_button);

        if (this.search.getBuilding()) {
            this.button.setProgress(10);
        }

        this.button.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchViewModel.this.startSearch();
            }
        });
        this.button.build();

        // Already building
        if (search.getBuilding()) {
            SearchViewModel.this.button.setProgress(50);
        }
    }

    private void startSearch() {
        this.button.setEnabled(false);


        Call<ActionResponse> request = this.service.searchBuild(SharedPreferencesHelper.getToken(this.context), this.search.getSearchId());
        request.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                SearchViewModel.this.button.setEnabled(true);

                if (!response.isSuccessful()) {
                    Toast.makeText(context, Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                SearchViewModel.this.search.setBuilding(true);
                SearchViewModel.this.button.setProgress(10);//TODO handler for time
            }

            // Network error
            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {
                Toast.makeText(context, R.string.error_network, Toast.LENGTH_LONG).show();
                SearchViewModel.this.button.setEnabled(true);
            }
        });
    }


    @Bindable
    public Boolean getSearchBuildLoading() {
        return searchBuildLoading;
    }

    public void setSearchBuildLoading(Boolean seachBuildLoading) {
        this.searchBuildLoading = seachBuildLoading;
        notifyPropertyChanged(BR.searchBuildLoading);
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
