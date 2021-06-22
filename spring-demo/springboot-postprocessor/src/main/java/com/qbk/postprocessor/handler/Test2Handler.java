package com.qbk.postprocessor.handler;

import com.qbk.postprocessor.annotation.TestBean;
import org.springframework.stereotype.Component;

@Component
@TestBean(name = "test2")
public class Test2Handler implements ParentHandler{

    @Override
    public String getName(){
        TestBean annotation = Test2Handler.class.getAnnotation(TestBean.class);
        return annotation.name();
    }

}
