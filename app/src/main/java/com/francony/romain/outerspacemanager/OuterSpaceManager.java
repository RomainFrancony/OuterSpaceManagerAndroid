package com.francony.romain.outerspacemanager;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class OuterSpaceManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
