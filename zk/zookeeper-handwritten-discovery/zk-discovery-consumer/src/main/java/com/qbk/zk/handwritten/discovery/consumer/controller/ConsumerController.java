package com.qbk.zk.handwritten.discovery.consumer.controller;

import com.qbk.zk.handwritten.discovery.consumer.discovery.ServiceDiscovery;
import com.qbk.zk.handwritten.discovery.consumer.loadbalance.LoadBalance;
import com.qbk.zk.handwritten.discovery.consumer.loadbalance.RandomLoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @RequestMapping("/query")
    public String query(){
        //服务发现
        List<String> urls = this.serviceDiscovery.discovery("discovery-provider");
        //负载均衡
        LoadBalance loadBalance = new RandomLoadBalance();
        String url = loadBalance.select(urls);
        System.out.println("目标url为: " + url);
        //请求
        String response = new RestTemplate().getForObject("http://" + url + "/provider/query", String.class);
        System.out.println("response: " + response);
        return response;
    }
}
