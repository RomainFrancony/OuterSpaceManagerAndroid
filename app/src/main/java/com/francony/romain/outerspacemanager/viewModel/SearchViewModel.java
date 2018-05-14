package com.francony.romain.outerspacemanager.viewModel;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Progress;
import com.francony.romain.outerspacemanager.model.Progress_Table;
import com.francony.romain.outerspacemanager.model.Search;
import com.francony.romain.outerspacemanager.BR;
import com.francony.romain.outerspacemanager.response.ActionResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.hkm.ui.processbutton.iml.SubmitProcessButton;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends BaseObservable {
    private Search search;
    private View view;
    private Context context;
    private Progress progress;
    private Handler handler;
    private ModelAdapter<Progress> progressModelAdapter = FlowManager.getModelAdapter(Progress.class);
    private SubmitProcessButton button;
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    public SearchViewModel(Search search, View view, Context context) {
        this.search = search;
        this.view = view;
        this.context = context;

        this.initButtonProgress();
        this.getProgress();

        // Already building
        if (search.getBuilding()) {

            this.button.setEnabled(false);
            this.button.setProgress(1);

            if(this.progress == null){
                return;
            }

            this.initCountdown();

        }
    }

    private void getProgress() {
        ArrayList<Progress> progresses = (ArrayList<Progress>) SQLite.select().from(Progress.class)
                .where(Progress_Table.type.eq(Progress.TYPE_SEARCH))
                .and(Progress_Table.constructionObjectId.eq(this.getSearch().getSearchId()))
                .queryList();



        progresses.sort(new Comparator<Progress>() {
            @Override
            public int compare(Progress o1, Progress o2) {
                return (int)(o2.getEndTime() - o1.getEndTime());
            }
        });

        ArrayList<Progress> toDelete = new ArrayList<>();

        for (Progress progress : progresses) {
            if( progress.getEndTime() <= System.currentTimeMillis() / 1000){
                toDelete.add(progress);
            }
        }
        this.progressModelAdapter.deleteAll(toDelete);

        if (progresses.isEmpty()){
            return;
        }

        this.progress = progresses.get(0);
    }

    private void initButtonProgress() {
        this.button = view.findViewById(R.id.search_action_button);
        this.button.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchViewModel.this.startSearch();
            }
        });
        this.button.build();
    }


    private void updateCountdown() {
        long startTime = this.progress.getEndTime() - this.getSearch().getTimeToBuild();
        int progress = (int)(((System.currentTimeMillis()/1000) - startTime) * 100 / (this.progress.getEndTime() - startTime));
        Log.wtf("ah", progress+"");
        this.button.setProgress(progress <= 0 ? 1 : progress);
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
                SearchViewModel.this.button.setProgress(1);
                SearchViewModel.this.button.setEnabled(false);
                SearchViewModel.this.saveSearchTime();
            }

            // Network error
            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {
                Toast.makeText(context, R.string.error_network, Toast.LENGTH_LONG).show();
                SearchViewModel.this.button.setEnabled(true);
            }
        });
    }

    private void saveSearchTime() {
        this.progressModelAdapter = FlowManager.getModelAdapter(Progress.class);
        this.progress = new Progress(UUID.randomUUID(), this.getSearch().getTimeToBuild() + (System.currentTimeMillis() / 1000), Progress.TYPE_SEARCH, this.getSearch().getSearchId());
        this.progressModelAdapter.insert(this.progress);
        this.initCountdown();
    }


    private void initCountdown() {
        if(this.handler == null){
            this.handler = new Handler();
        }

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                if(SearchViewModel.this.button.getProgress() >= 100 ) {
                    SearchViewModel.this.button.setEnabled(true);
                    SearchViewModel.this.button.setProgress(0);
                    SearchViewModel.this.progressModelAdapter.delete(SearchViewModel.this.progress);
                    SearchViewModel.this.progress = null;
                    SearchViewModel.this.getSearch().setBuilding(false);
                    SearchViewModel.this.getSearch().setLevel(SearchViewModel.this.getSearch().getLevel() + 1);
                    return;
                }

                SearchViewModel.this.updateCountdown();
                SearchViewModel.this.handler.postDelayed( this, (SearchViewModel.this.getSearch().getTimeToBuild() / 100) * 1000 );
            }
        }, 0 );
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
