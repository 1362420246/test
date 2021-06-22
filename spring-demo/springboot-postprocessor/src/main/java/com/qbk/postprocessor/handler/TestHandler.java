package com.qbk.postprocessor.handler;

import com.qbk.postprocessor.annotation.TestBean;
import org.springframework.stereotype.Component;

@Component
@TestBean(name = "test")
public class TestHandler implements ParentHandler{

    @Override
    public String getName(){
        TestBean annotation = TestHandler.class.getAnnotation(TestBean.class);
        return annotation.name();
    }

}
