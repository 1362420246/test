package com.qbk.enumdemo.paramconver;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 传参 枚举 转换
 */
@RestController
public class VipController {

    /**
     * {
     *   "id": 9527,
     *   "name":"kk",
     *    "vip": 2
     *  }
     */
    @PostMapping("/vip")
    public UserDTO convert(@RequestBody UserDTO user) {
        System.out.println(user);
        System.out.println(user.getVip());
        System.out.println(user.getVip().getValue());
        System.out.println(user.getVip().name());
        return user;
    }

}
