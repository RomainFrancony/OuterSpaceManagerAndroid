package com.francony.romain.outerspacemanager.services;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class OuterSpaceManagerServiceFactory {
    private static String API_URL = "https://outer-space-manager-staging.herokuapp.com/api/v1/";

    public static OuterSpaceManagerService create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OuterSpaceManagerServiceFactory.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(OuterSpaceManagerService.class);
    }
}
