package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
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
import com.francony.romain.outerspacemanager.viewModel.SearchViewModel;
import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.hkm.ui.processbutton.iml.SubmitProcessButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {
    private ArrayList<Search> searchesDataset;
    private Context context;

    public SearchAdapter(ArrayList<Search> searches, Context context) {
        this.searchesDataset = searches;
        this.context = context;
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
        holder.bind(search, this.context);
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

        public void bind(final Search search , final Context context){

            SearchViewModel searchViewModel = new SearchViewModel(search,binding.getRoot(),context);
            binding.setSearchViewModel(searchViewModel);
            binding.executePendingBindings();
        }
    }
}
