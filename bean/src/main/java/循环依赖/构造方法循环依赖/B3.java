package 循环依赖.构造方法循环依赖;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 构造方法循环依赖
 */
@AllArgsConstructor
@RestController
public class B3 {

    private A3 a3;

    @GetMapping("/b/3")
    public String get(){
        return a3 == null ? "null" : a3.toString();
    }
}
