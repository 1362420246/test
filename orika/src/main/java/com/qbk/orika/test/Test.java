package com.qbk.orika.test;

import com.qbk.orika.bean.User;
import com.qbk.orika.bean.UserExt;
import com.qbk.orika.bean.UserVO;
import com.qbk.orika.util.BeanCopierUtil;
import com.qbk.orika.util.BeanMapper;
import com.qbk.orika.util.OrikaUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试
 */
public class Test {
    public static void main(String[] args) {
        //测试 BeanCopier 复制相同字段
        User user = User.builder().id(1).name("kk").roles(Arrays.asList("admin","user")).build();
        System.out.println("原始字段： " + user);
        UserExt userExt = BeanCopierUtil.copyBean(user, UserExt.class);
        System.out.println("BeanCopier 测试：" + userExt);

        //测试Orika 不同字段
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("name","username");
        fieldMap.put("roles","roleList");
        UserVO userVO = OrikaUtil.tran(user, UserVO.class, fieldMap);
        System.out.println("Orika 测试：" + userVO);

        //测试Orika2
        //相同字段
        UserExt userExt1 = BeanMapper.map(user, UserExt.class);
        System.out.println("Orika2 测试：" + userExt1);
        UserExt userExt2 = BeanMapper.map(
                user,
                BeanMapper.getType(User.class),
                BeanMapper.getType(UserExt.class));
        System.out.println("Orika2 测试：" + userExt2);

    }
}
