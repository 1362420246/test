package com.qbk.longpolling.controller;

import com.qbk.longpolling.service.LongPollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 模拟 nacos 配置中心 长轮询
 *
 * 使用 AsyncContext + ScheduledExecutorService +事件机制  实现长轮询
 */
@RestController
public class LongPollingController {

    @Autowired
    private LongPollingService longPollingService;

    /**
     * 长轮询 监听
     */
    @RequestMapping("/listener")
    public void listener(HttpServletRequest request, HttpServletResponse response){
        //添加长轮询客户端
        longPollingService.addLongPollingClient(request, response);
    }

    /**
     * 变更数据
     */
    @RequestMapping("/dataChange")
    public String dataChange(){
        longPollingService.dataChange();
        return "变更成功";
    }
}
