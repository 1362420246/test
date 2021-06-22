package com.qbk.spring.listener.demo.listener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * ApplicationRunner后被回调
 * 和ApplicationRunner参数不一样
 */
@Component
public class HelloCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner");
    }
}
