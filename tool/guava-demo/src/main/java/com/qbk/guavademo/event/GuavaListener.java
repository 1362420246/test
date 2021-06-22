package com.qbk.guavademo.event;


import com.google.common.eventbus.Subscribe;
/**
 * 观察者
 */
public class GuavaListener {

    @Subscribe
    public void guavaListener(String msg){
        System.out.println("收到guava通知: " + msg);
    }

}