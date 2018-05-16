package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterSearchBinding;
import com.francony.romain.outerspacemanager.model.Search;
import com.francony.romain.outerspacemanager.viewModel.SearchViewModel;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {
    private ArrayList<Search> searchesDataset;
    private Context context;
    private RecyclerView recyclerView;

    public SearchAdapter(ArrayList<Search> searches, Context context) {
        this.searchesDataset = searches;
        this.context = context;
    }


    /**
     * Create view holder with data binding
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterSearchBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_search, parent, false);
        return new SearchAdapterViewHolder(binding);
    }

    /**
     * Bind building to view holder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(SearchAdapterViewHolder holder, int position) {
        Search search = this.searchesDataset.get(position);
        holder.bind(search, this.context);
    }

    /**
     * Get the attached recycler view
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        this.initRecyclerViewListener();
    }

    /**
     * Prevent view models to continue updating the UI when the view is detached from the recycler
     * while scrolling (onBindViewHolder is not necessary called and therefore no view model is created)
     */
    private void initRecyclerViewListener() {
        this.recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                SearchAdapterViewHolder holder = (SearchAdapterViewHolder) SearchAdapter.this.recyclerView.getChildViewHolder(view);
                holder.searchViewModel.setViewVisible(true);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                SearchAdapterViewHolder holder = (SearchAdapterViewHolder) SearchAdapter.this.recyclerView.getChildViewHolder(view);
                holder.searchViewModel.setViewVisible(false);
            }
        });
    }

    /**
     * Stop all view model from updating the UI when quiting the fragment
     *
     * @param recyclerView
     */
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final SearchAdapterViewHolder holder = (SearchAdapterViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            holder.searchViewModel.setViewVisible(false);
        }
    }

    @Override
    public int getItemCount() {
        return searchesDataset.size();
    }


    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder {
        private final AdapterSearchBinding binding;
        private SearchViewModel searchViewModel;

        public SearchAdapterViewHolder(AdapterSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Bind building to UI
         * @param search
         * @param context
         */
        public void bind(final Search search, final Context context) {
            this.searchViewModel = new SearchViewModel(search, binding.getRoot(), context);
            binding.setSearchViewModel(this.searchViewModel);
            binding.executePendingBindings();
        }
    }
}
