package com.francony.romain.outerspacemanager.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.OuterSpaceManagerDatabase;
import com.francony.romain.outerspacemanager.databinding.NavHeaderMainBinding;
import com.francony.romain.outerspacemanager.fragment.GalaxyFragment;
import com.francony.romain.outerspacemanager.fragment.AttacksFragment;
import com.francony.romain.outerspacemanager.fragment.BuildingsFragment;
import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.fragment.HomeFragment;
import com.francony.romain.outerspacemanager.fragment.SearchesFragment;
import com.francony.romain.outerspacemanager.fragment.SpaceyardFragment;
import com.francony.romain.outerspacemanager.fragment.UserInfoFragment;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.response.UserInfoResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int SPACEYARD_DRAWER_INDEX = 2;
    public static final int GALAXY_DRAWER_INDEX = 5;
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private DrawerLayout drawer;
    public NavigationView navigationView;
    private NavHeaderMainBinding navHeaderBinding;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Drawer
        this.drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(toggle);
        toggle.syncState();
        this.navigationView = findViewById(R.id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);


        // Initial fragment content
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        try {
            fragment = HomeFragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);


        navHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(navHeaderBinding.getRoot());
        this.initRefreshInfo();
    }


    private void initRefreshInfo() {
        this.handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (MainActivity.this.isDestroyed()) {
                    return;
                }

                MainActivity.this.getUserInfos();
                MainActivity.this.handler.postDelayed(this, 5000);
            }
        }, 0);
    }



    private void getUserInfos() {
        Call<UserInfoResponse> request = this.service.userInfo(SharedPreferencesHelper.getToken(getApplicationContext()));
        request.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                navHeaderBinding.setUser(response.body());
            }

            // Network error
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
            }
        });
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            // Cancel back button if clicked when on other fragment than home
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof HomeFragment) {
                    super.onBackPressed();
                    return;
                }
            }
            // Initial fragment content
            this.displayInitialFragment();
        }
    }


    private void displayInitialFragment() {
        this.navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
        this.navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Class fragmentClass = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_buildings) {
            fragmentClass = BuildingsFragment.class;
        } else if (id == R.id.nav_spaceyard) {
            fragmentClass = SpaceyardFragment.class;
        } else if (id == R.id.nav_search) {
            fragmentClass = SearchesFragment.class;
        } else if (id == R.id.nav_attacks) {
            fragmentClass = AttacksFragment.class;
        } else if (id == R.id.nav_galaxy) {
            fragmentClass = GalaxyFragment.class;
        } else if (id == R.id.nav_logout) {
            this.logout();
            this.drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Don't update the fragment if it's the same a the current one
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_content);
        if (currentFragment.getClass() == fragmentClass) {
            this.drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        // Replace with the new fragment
        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commit();

        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        FlowManager.getDatabase(OuterSpaceManagerDatabase.class).reset();
        SharedPreferencesHelper.clearToken(getApplicationContext());
        SharedPreferencesHelper.clearTokenExpiration(getApplicationContext());
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        this.finish();
    }
}
