package 循环依赖.构造方法属性循环依赖;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性 + 构造方法循环依赖
 * 根据 构造注入、属性注入 谁先实例化会产生两种不同结果，假设例子以a先实例化进行：
 * 1、a属性 + b构造  spring会处理循环依赖
 * 2、a构造 + b属性  会发生循环依赖，因为实例化a时候需要b，b注入a实例，a还在实例化中
 * 关于2中的解决：使用@Lazy注解让a延时加载，或在b中注入加@Lazy注解延时注入，可以解决
 */
@Lazy
@AllArgsConstructor
@RestController
public class A2 {

//    @Autowired
    private B2 b2;

    @GetMapping("/a/2")
    public String get(){
        return b2.toString();
    }
}
