package com.qbk.enumdemo.params;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 传参 枚举
 */
@RestController
public class SexController {

    /**
     *   默认json是按照 下标 或者 枚举名 转换
     *   {
     *       "id": 9527,
     *        "name":"kk",
     *        "sex": 0
     *    }
     *  或者
     *  {
     *    "id": 9527,
     *    "name":"kk",
     *     "sex": "MEN"
     *   }
     */
    @PostMapping("/add")
    public UserDTO add(@RequestBody UserDTO user){
        System.out.println(user);
        System.out.println(user.getSex());
        System.out.println(user.getSex().getValue());
        System.out.println(user.getSex().name());
        return user;
    }
}
