package com.springboot.retrofit.controller;

import com.springboot.retrofit.api.TestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private TestApi testApi;

    @GetMapping("/get")
    public Map<String,Object> get() throws IOException, InterruptedException {
        Map<String, Object> result;
        int id = 36;

        Call<Map<String,Object>> call = testApi.testGet(id);

        //同步
//        Response<Map<String, Object>> execute = stringCall.execute();
//        result = execute.body();
//        System.out.println(result);

        //异步
        result = new HashMap<>();
        call.enqueue(new Callback<Map<String,Object>>() {
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String, Object> body = response.body();
                System.out.println(body);
            }

            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        TimeUnit.SECONDS.sleep(3);

        result.put("code",200);
        return result;
    }

    @GetMapping("/post")
    public Map<String,Object> post() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("code",405);

        Call<Map<String, Object>> call = testApi.testPost(params);
        Response<Map<String, Object>> response = call.execute();

        System.out.println(response.code());

        Map<String, Object> body = response.body();

        System.out.println(body);
        return body;
    }
}
