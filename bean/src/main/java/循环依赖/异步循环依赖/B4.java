package 循环依赖.异步循环依赖;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class B4 {

    @Autowired
    private A4 a;

    @GetMapping("/b")
    public String get(){
        return a.toString();
    }
}
