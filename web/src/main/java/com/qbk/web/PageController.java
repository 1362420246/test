package com.qbk.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：quboka
 * @description： spring-data 的 PageableDefault注解
 * @date ：2019/12/5 14:28
 */
@RestController
public class PageController {

    @GetMapping("/page")
    public String page(@PageableDefault(page = 5 ,
                                        size = 15 ,
                                        sort = {"user.age","user.name"},
                                        direction = Sort.Direction.DESC
                                        ) Pageable pageable){
        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getSort());
        return "s";
    }
}
