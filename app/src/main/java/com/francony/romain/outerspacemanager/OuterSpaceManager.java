package com.francony.romain.outerspacemanager;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class OuterSpaceManager extends Application {
    private static OuterSpaceManager instance;
    public static OuterSpaceManager getInstance() { return instance; }

    @Override
    public void onCreate() {

        super.onCreate();
        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
        instance = this;
    }
}
