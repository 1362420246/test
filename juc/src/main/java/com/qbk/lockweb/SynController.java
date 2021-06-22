package com.qbk.lockweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * synchronized 限流测试
 */
@RestController
@RequestMapping("/syn")
public class SynController {
    /**
     * 16进制的1
     */
    private volatile static byte value = 0x001;

    public void unlock(){
        //非运算符用符号“~”表示，其运算规律如下： 如果位为0，结果是1，如果位为1，结果是0
         value = (byte)~ value;
    }

    public synchronized boolean lock(){
        if( value < 0){
            return false;
        }
        // ~1 = -2
        value = (byte)~ value;
        return true;
    }

    @GetMapping("/get")
    public String get() throws Exception {
        if(lock()){
           try {
               TimeUnit.SECONDS.sleep(2);
               return "成功";
           }finally {
               unlock();
           }
        }else {
            return "稍后再试";
        }
    }

}
