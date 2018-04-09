package com.francony.romain.outerspacemanager.services;


import com.francony.romain.outerspacemanager.model.User;
import com.francony.romain.outerspacemanager.response.ActionResponse;
import com.francony.romain.outerspacemanager.response.BuildingsResponse;
import com.francony.romain.outerspacemanager.response.Scoreboard;
import com.francony.romain.outerspacemanager.response.SearchesResponse;
import com.francony.romain.outerspacemanager.response.ShipBuildingResponse;
import com.francony.romain.outerspacemanager.response.SpaceyardResponse;
import com.francony.romain.outerspacemanager.response.UserInfoResponse;
import com.francony.romain.outerspacemanager.response.UserResponse;
import com.francony.romain.outerspacemanager.response.reports.ReportListResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OuterSpaceManagerService {
    @POST("auth/create")
    Call<UserResponse> userSignIn(@Body User user);

    @POST("auth/login")
    Call<UserResponse> userLogin(@Body User user);

    @GET("users/get")
    Call<UserInfoResponse> userInfo(@Header("x-access-token") String token);

    @GET("buildings/list")
    Call<BuildingsResponse> buildingList(@Header("x-access-token") String token);

    @POST("buildings/create/{id}")
    Call<ActionResponse> buildingBuild(@Header("x-access-token") String token, @Path("id") Integer buildingId);

    @GET("users/{from}/{limit}")
    Call<Scoreboard> userList(@Header("x-access-token") String token, @Path("from") Integer from, @Path("limit") Integer limit);

    @GET("ships")
    Call<SpaceyardResponse> spaceyardList(@Header("x-access-token") String token);

    @POST("ships/create/{id}")
    Call<ShipBuildingResponse> shipBuild(@Header("x-access-token") String token, @Path("id") int id, @Body HashMap<Object, Object> data);

    @GET("fleet/list")
    Call<SpaceyardResponse> fleetList(@Header("x-access-token") String token);

    @GET("searches/list")
    Call<SearchesResponse> searchList(@Header("x-access-token") String token);

    @POST("searches/create/{id}")
    Call<ActionResponse> searchBuild(@Header("x-access-token") String token, @Path("id") int id);


    @GET("users/{from}/{limit}")
    Call<ReportListResponse> reportsList(@Header("x-access-token") String token, @Path("from") Integer from, @Path("limit") Integer limit);
}
