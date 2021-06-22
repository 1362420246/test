package com.qbk.pattern.chain.controller;

import com.qbk.pattern.chain.handler.AbstractRoomHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 责任链方式二：节点控制模式
 * 链的每个节点都包含对下一个节点的引用 ，每个节点调用完成之后，内部逻辑决定是否继续调用下一个节点。
 */
@RequestMapping("/handler")
@RestController
public class HandlerController {

    @Autowired
    private AbstractRoomHandler applyAccountHandler;

    @Autowired
    private AbstractRoomHandler queryRoomHandler;

    @Autowired
    private AbstractRoomHandler createRoomHandler;

    @Autowired
    private AbstractRoomHandler enterRoomHandler;

    @PostConstruct
    public void init(){
        //初始化节点关系
        applyAccountHandler.setNextHanlder(queryRoomHandler);
        queryRoomHandler.setNextHanlder(createRoomHandler);
        createRoomHandler.setNextHanlder(enterRoomHandler);
    }

    @GetMapping("/room")
    public String login(String roomid){
        if(applyAccountHandler.handleRequest(roomid)){
            return "成功";
        }
        return "失败";
    }
}
