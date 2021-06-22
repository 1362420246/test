package 循环依赖.三个循环依赖;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class C1 {

    private A1 a;

    @GetMapping("/a")
    public String get(){
        return a.toString();
    }
}
