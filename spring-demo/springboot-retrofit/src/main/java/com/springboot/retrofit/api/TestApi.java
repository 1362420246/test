package com.springboot.retrofit.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

public interface TestApi {

    /**
     * GET请求
     */
    @GET("/test/api/get")
    Call<Map<String,Object>> testGet(@Query("id") Integer id);

    /**
     * POST请求
     */
    @POST("/test/api/post")
    Call<Map<String,Object>> testPost(@Body Map<String,Object> params);
}
