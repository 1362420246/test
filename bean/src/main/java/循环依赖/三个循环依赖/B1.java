package 循环依赖.三个循环依赖;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class B1 {

    private C1 c;

    @GetMapping("/c")
    public String get(){
        return c.toString();
    }
}
