package 循环依赖.构造方法循环依赖;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 构造方法循环依赖
 * spring无法解决的循环依赖
 */
@AllArgsConstructor
@RestController
public class A3 {

    private B3 b3;

    @GetMapping("/a/3")
    public String get(){
        return b3 == null ? "null" : b3.toString();
    }
}
