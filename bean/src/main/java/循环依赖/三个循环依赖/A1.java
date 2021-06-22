package 循环依赖.三个循环依赖;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 保证第一个初始化的类 IoC 和 DI 分开就可以，使用 属性注入 代替 构造器注入
 * 实例化 和 注入 分开，就可以使用spring三级缓存避免循环依赖
 *  1、a实例化
 *  2、a注入b -> b实例化 -> b注入c -> c实例化
 *  3、c注入a(从缓存中获取半成品a)
 *  4、c实例化完成 -> b实例化完成 -> a实例化完成
 */
//@AllArgsConstructor
@RestController
public class A1 {

    @Autowired
    private B1 b;

    @GetMapping("/b")
    public String get(){
        return b.toString();
    }
}
