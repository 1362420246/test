package 循环依赖.异步循环依赖;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  @Async 注解时循环依赖 异常：
 * Error creating bean with name 'a4': Bean with name 'a4' has been injected into other beans [b4] in its raw version as part of a circular reference,
 * but has eventually been wrapped. This means that said other beans do not use the final version of the bean.
 * This is often the result of over-eager type matching - consider using 'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.
 */
@RestController
public class A4 {

    /**
     * 添加 @Lazy 注解 解决循环依赖
     */
    //@Lazy
    @Autowired
    private B4 b;

    @Async
    @GetMapping("/a")
    public String get(){
        return b.toString();
    }
}
