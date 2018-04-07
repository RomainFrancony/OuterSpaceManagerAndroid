package com.francony.romain.outerspacemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterSearchBinding;
import com.francony.romain.outerspacemanager.model.Search;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {
    private ArrayList<Search> searchesDataset;

    public SearchAdapter(ArrayList<Search> searches) {
        this.searchesDataset = searches;
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
        holder.bind(search);
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

        public void bind(Search search ){
            binding.setSearch(search);
            binding.executePendingBindings();
        }
    }
}
