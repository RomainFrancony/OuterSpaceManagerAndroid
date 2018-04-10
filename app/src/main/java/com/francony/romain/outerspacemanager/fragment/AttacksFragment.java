package com.francony.romain.outerspacemanager.fragment;


import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Report;
import com.francony.romain.outerspacemanager.response.ReportListResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AttacksFragment extends Fragment {
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Report> reportList;

    public AttacksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attacks, container, false);


        viewPager = v.findViewById(R.id.viewpager);
        setViewPager(viewPager);

        tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        this.getReports();

        return v;
    }


    private void getReports() {

        Call<ReportListResponse> request = this.service.reportsList(SharedPreferencesHelper.getToken(getContext()), 0, 20);
        request.enqueue(new Callback<ReportListResponse>() {
            @Override
            public void onResponse(Call<ReportListResponse> call, Response<ReportListResponse> response) {
                AttacksFragment.this.reportList = response.body().getReports();
                AttacksFragment.this.reportsFragment.setReports(response.body().getReports());
            }

            // Network error
            @Override
            public void onFailure(Call<ReportListResponse> call, Throwable t) {
            }
        });


    }





    public ArrayList<Report> getReportList() {
        return reportList;
    }


    private BuildingsFragment buildingsFragment;
    private ReportsFragment reportsFragment;

    private void setViewPager(ViewPager viewPager) {
        buildingsFragment = new BuildingsFragment();
        reportsFragment = new ReportsFragment();


        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(buildingsFragment, getString(R.string.attacks_in_progress));
        adapter.addFragment(reportsFragment, getString(R.string.attacks_completed));
        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
