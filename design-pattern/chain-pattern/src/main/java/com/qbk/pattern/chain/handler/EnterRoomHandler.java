package com.qbk.pattern.chain.handler;

import org.springframework.stereotype.Component;

/**
 * 4进入会议室
 */
@Component
public class EnterRoomHandler extends AbstractRoomHandler{

    @Override
    public boolean handleRequest(String request) {
        System.out.println("进入会议室成功");
        return true;
    }
}
