package com.qbk.pattern.template.template;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 模板方法+函数式接口 结合
 */
public class LogTemplate {
    public static void main(String[] args) {
        String log1 = "info:1";
        String log2 = "error:1";
        String log3 = "info:2";
        String log4 = "error:2";
        String log5 = "info:3";
        String log6 = "error:3";
        List<String> logList = Stream.of(log1,log2,log3,log4,log5,log6).collect(Collectors.toList());
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String log:logList) {
            stringJoiner.add(log);
        }
        final String logs = stringJoiner.toString();

        LogTemplate logTemplate = new LogTemplate();
        logTemplate.info(logs);
        System.out.println("------------------------------------");
        logTemplate.error(logs);
    }

    /**
     * 模板方法
     * @param consumer 代表了接受一个输入参数并且无返回的操作
     */
    public void outputLog(String logs , Consumer<String> consumer){
        List<String> logList = Arrays.asList(logs.split(","));
        for (String log: logList) {
            consumer.accept(log);
        }
    }

    public void info(String logs){
        this.outputLog(
                logs,
                log ->{
                    if(log.startsWith("info")){
                        System.out.println(log);
                    }
                }
        );
    }

    public void error(String logs){
        this.outputLog(
                logs,
                log ->{
                    if(log.startsWith("error")){
                        System.out.println(log);
                    }
                }
        );
    }
}
