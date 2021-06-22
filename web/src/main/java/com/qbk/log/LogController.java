package com.qbk.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试 log打印
 */
@Slf4j
@RestController
public class LogController {

    @GetMapping("/print-log")
    public LogVO printLog(LogVO logVO, HttpServletRequest request){
        log.info("params = {}", logVO);
        log.info("request = {}", request);
        return logVO;
    }

}
