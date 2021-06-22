package com.qbk.pattern.chain.handler;

import org.springframework.stereotype.Component;

/**
 * 3创建会议室
 */
@Component
public class CreateRoomHandler extends AbstractRoomHandler{
    @Override
    public boolean handleRequest(String request) {
        System.out.println("创建会议室成功");
        return this.nextHandler.handleRequest(request);
    }
}
