package com.francony.romain.outerspacemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterSearchBinding;
import com.francony.romain.outerspacemanager.fragment.SearchesFragment;
import com.francony.romain.outerspacemanager.model.Search;
import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.hkm.ui.processbutton.iml.SubmitProcessButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {
    private ArrayList<Search> searchesDataset;
    private SearchesFragment searchesFragment;

    public SearchAdapter(ArrayList<Search> searches, SearchesFragment searchesFragment) {
        this.searchesDataset = searches;
        this.searchesFragment = searchesFragment;
    }


    @Override
    public SearchAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterSearchBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_search, parent, false);
        return new SearchAdapterViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(SearchAdapterViewHolder holder, int position) {
        Search search = this.searchesDataset.get(position);
        holder.bind(search, this.searchesFragment);
    }

    @Override
    public int getItemCount() {
        return searchesDataset.size();
    }



    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder {
        private final AdapterSearchBinding binding;

        public SearchAdapterViewHolder(AdapterSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Search search , final SearchesFragment searchesFragment){

            // Button event handling, can't do it via databinding because we need to build it before
            final SubmitProcessButton mBtnAction = binding.getRoot().findViewById(R.id.search_action_button);
            mBtnAction.build();

            // Already building
            if(search.getBuilding()){
                // TODO get time remaining
                mBtnAction.setProgress(50);
            }
            //TODO click handlers



            binding.setSearch(search);
            binding.executePendingBindings();
        }
    }
}
