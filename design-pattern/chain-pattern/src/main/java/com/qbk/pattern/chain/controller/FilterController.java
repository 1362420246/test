package com.qbk.pattern.chain.controller;

import com.qbk.pattern.chain.filter.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 责任链方式一：外部控制模式
 * 对于外部控制的方式，这种方式比较简单，链的每个节点只需要专注于各自的逻辑即可，
 * 而当前节点调用完成之后是否继续调用下一个节点，这个则由外部控制逻辑进行。
 */
@RequestMapping("/filter")
@RestController
public class FilterController {

    /**
     * 使用 @Order 控制数组里面的顺序
     * 注解@Order或者接口Ordered的作用是定义Spring IOC容器中Bean的执行顺序的优先级，而不是定义Bean的加载顺序
     */
    @Autowired
    private List<UserFilter> filters;

    @GetMapping("/login")
    public String login(String username ,String password){
        for (UserFilter filter :filters) {
            if(!filter.check(username,password)){
                return "失败";
            }
        }
        return "成功";
    }
}
