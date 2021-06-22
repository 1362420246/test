package com.qbk.advice;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebBindingInitializer;

import java.beans.PropertyEditorSupport;

/**
 * controller增强器
 */
@RestControllerAdvice
public class TestControllerAdvice  {

    /**
     *  InitBinder写在Controller内部只对当前处理器生效
     *  InitBinder结合@RestControllerAdvice 标注的方法对所有的Controller都是生效的。
     *  InitBinder对@RequestBody这种基于消息转换器的请求参数无效
     *  因为@InitBinder它用于初始化DataBinder数据绑定、类型转换等功能，
     *  而@RequestBody它的数据解析、转换时消息转换器来完成的，
     *  所以即使你自定义了属性编辑器，对它是不生效的
     *  （它的WebDataBinder只用于数据校验，不用于数据绑定和数据转换。它的数据绑定转换若是json，一般都是交给了jackson来完成的）
     *
     *  WebDataBinder 数据绑定
     *  WebDataBinder在SpringMVC中使用，它不需要我们自己去创建，
     *  我们只需要向它注册参数类型对应的属性编辑器PropertyEditor。
     *  PropertyEditor可以将字符串转换成其真正的数据类型，
     *  它的void setAsText(String text)方法实现数据转换的过程。
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //修改字符串的属性编辑器。  str去空格
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        //注册自定义编辑器
        binder.registerCustomEditor(Integer.class,new PropertyEditorSupport(){
            //属性编辑器
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                super.setValue(text+10);
            }
        });
    }

}
