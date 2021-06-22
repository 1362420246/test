package com.qbk.pattern.chain.handler;

import org.springframework.stereotype.Component;

/**
 * 2查询会议室
 */
@Component
public class QueryRoomHandler extends AbstractRoomHandler{

    private String roomid = "9527";

    @Override
    public boolean handleRequest(String request) {
        if(roomid.equals(request)){
            System.out.println("查询会议成功");
            return this.nextHandler.nextHandler.handleRequest(request);
        }else {
            System.out.println("查询会议失败");
            return this.nextHandler.handleRequest(request);
        }
    }
}
