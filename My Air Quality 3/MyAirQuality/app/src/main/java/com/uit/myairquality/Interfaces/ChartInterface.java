package com.uit.myairquality.Interfaces;

import com.google.gson.JsonArray;
import com.uit.myairquality.Model.ChartResquest;
import com.uit.myairquality.Model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChartInterface {

    @Headers("Content-Type: application/json")
    @POST("/api/master/asset/datapoint/{assetId}/attribute/{attributeName}")

 Call<JsonArray> callchart(
            @Path("assetId") String assetId,
            @Path("attributeName") String attributeName,
            @Header("Authorization") String authorization,
            @Body ChartResquest request
    );

}
