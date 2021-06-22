package 循环依赖.构造方法属性循环依赖;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性 + 构造方法循环依赖
 * a属性 + b构造
 * b属性 + a构造
 * 两种结果不同
 */
//@AllArgsConstructor
@RestController
public class B2 {

    //@Lazy
    @Autowired
    private A2 a2;

    @GetMapping("/b/2")
    public String get(){
        return a2.toString();
    }
}
