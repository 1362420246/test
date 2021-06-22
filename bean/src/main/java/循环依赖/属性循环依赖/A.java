package 循环依赖.属性循环依赖;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性循环依赖。
 * spring使用三级缓存可以自动解决
 */
@RestController
public class A {

    @Autowired
    private B b;

    @GetMapping("/a/1")
    public String get(){
        return b.toString();
    }
}
