package com.qbk.easyexceldemo.util;

import com.alibaba.excel.annotation.ExcelProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 校验器
 */
public class BeanValidator {

    /**
     * 校验工厂
     */
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 校验，抛出异常
     */
    public static void check(Object obj) {
        Map<String, String> result = BeanValidator.validateObject(obj);
        if(!MapUtils.isEmpty(result)){
            result.forEach((k,v) ->{
                throw new RuntimeException(k + ":" + v);
            });
        }
    }

    /**
     * 通用的校验
     */
    public static Map<String,String>validateObject(Object obj){
        if(obj instanceof Collection){
            return BeanValidator.validateList((Collection)obj);
        }else{
            return BeanValidator.validate(obj);
        }
    }

    /**
     * 校验集合
     */
    public static Map<String,String> validateList(Collection<?>collection){
        //校验collection是否为空
        Preconditions.checkNotNull(collection);
        Iterator<?> iterator = collection.iterator();
        Map<String,String> errors;
        do{
            if(!iterator.hasNext()){
                return Collections.emptyMap();
            }else{
                Object next = iterator.next();
                errors = validate(next);
            }
        }while(errors.isEmpty());
        return errors;
    }

    /**
     * 校验方法
     * @param t 校验对象
     * @param groups 参数类型
     * @return 返回错误字段和信息
     */
    public static <T>Map<String,String> validate(T t,Class... groups){
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> result = validator.validate(t, groups);
        if(result.isEmpty()){
            return Collections.emptyMap();
        }else{
            LinkedHashMap<String,String> errors = Maps.newLinkedHashMap();
            for (ConstraintViolation<T> violation : result) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    public static <T> String validateEntity(T obj) {
        StringBuilder result = new StringBuilder();
        Set<ConstraintViolation<T>> set = validatorFactory.getValidator().validate(obj, Default.class);
        if (set != null && !set.isEmpty()) {
            try {
                for (ConstraintViolation<T> cv : set) {
                    Field declaredField = obj.getClass().getDeclaredField(cv.getPropertyPath().toString());
                    ExcelProperty annotation = declaredField.getAnnotation(ExcelProperty.class);
                    //拼接错误信息，包含当前出错数据的标题名字+错误信息
                    result.append(annotation.value()[0]).append(cv.getMessage()).append(";");
                }
            } catch (Exception e) {
                result.append(e.getMessage());
            }
        }
        return result.toString();
    }
}
