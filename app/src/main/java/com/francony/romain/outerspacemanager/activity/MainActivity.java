package com.francony.romain.outerspacemanager.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.francony.romain.outerspacemanager.fragment.AttacksFragment;
import com.francony.romain.outerspacemanager.fragment.BuildingsFragment;
import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.fragment.HomeFragment;
import com.francony.romain.outerspacemanager.fragment.SearchesFragment;
import com.francony.romain.outerspacemanager.fragment.SpaceyardFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Drawer
        this.drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, this.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        }else if (id == R.id.nav_logout) {
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
        if(currentFragment.getClass() == fragmentClass){
            this.drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        // Replace with the new fragment
        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commit();

        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
    }
}
