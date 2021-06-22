package com.springboot.retrofit.config;

import com.springboot.retrofit.api.TestApi;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestAdapterConfig {

    @Bean
    public Retrofit retrofit2() {
        OkHttpClient build = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8080")
                .addConverterFactory(GsonConverterFactory.create())
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();
        return retrofit2;
    }

    @Bean
    public TestApi testApi(Retrofit retrofit2){
        return retrofit2.create(TestApi.class);
    }
}