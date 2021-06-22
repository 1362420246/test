package com.qbk.pattern.chain.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 1申请账号
 */
@Component
public class ApplyAccountHandler extends AbstractRoomHandler{
    @Override
    public boolean handleRequest(String request) {
        if(StringUtils.isEmpty(request)){
            System.out.println("申请账号失败");
            return false;
        }
        System.out.println("申请账号成功");
        return this.nextHandler.handleRequest(request);
    }
}
