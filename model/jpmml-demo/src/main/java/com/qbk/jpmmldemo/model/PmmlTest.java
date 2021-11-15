package com.qbk.jpmmldemo.model;

import com.google.common.collect.RangeSet;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PmmlTest {

    public static void main(String[] args) throws JAXBException, IOException, SAXException {
        //从一个PMML文件构建一个模型评估器
        Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                .load(new File("C:\\Users\\86186\\Desktop\\testpmml.pmml"))
                .build();

        //执行自检
        evaluator.verify();

        //打印输入(x1, x2， ..xn)字段
        List<? extends InputField> inputFields = evaluator.getInputFields();
        System.out.println("*** Input fields: " + inputFields.size());
//        inputFields.forEach(System.out::println);

        //查询和分析输入字段
        for (InputField inputField : inputFields) {
            org.dmg.pmml.DataField pmmlDataField = (org.dmg.pmml.DataField) inputField.getField();
            org.dmg.pmml.MiningField pmmlMiningField = inputField.getMiningField();

            org.dmg.pmml.DataType dataType = inputField.getDataType();
            org.dmg.pmml.OpType opType = inputField.getOpType();

//            System.out.println(pmmlDataField.getName());
//            System.out.println(pmmlMiningField.getName());
//            System.out.println(dataType);
//            System.out.println(opType);

            //数值、顺序和分类字段
            //连续（continuous）特征；
            //无序类别（categorical）特征；
            //有序类别（ordinal）特征。
            switch (opType) {
                case CONTINUOUS: //连续
                    //返回此连续字段的有效值域
                    RangeSet<Double> validInputRanges = inputField.getContinuousDomain();
                    System.out.println("continuous  " + pmmlDataField.getName().getValue() + " : " + validInputRanges);
                    break;
                case CATEGORICAL: //无序
                    //返回此类别或序号字段的有效值域
                    List<?> validInputValues = inputField.getDiscreteDomain();
                    System.out.println("categorical  " + pmmlDataField.getName().getValue() + " : " + validInputValues);
                    break;
                case ORDINAL: //序数
                    //返回此类别或序号字段的有效值域
                    List<?> discreteDomain = inputField.getDiscreteDomain();
                    System.out.println("ordinal  " + pmmlDataField.getName().getValue() + " : " + discreteDomain);
                    break;
                default:
                    break;
            }
        }

        //打印主要结果字段(y)
        List<? extends TargetField> targetFields = evaluator.getTargetFields();
        System.out.println("*** Target field(s): " + targetFields + " :  " + targetFields.get(0).getCategories());

        //打印次要结果(如。概率(y),决策(y))领域
        List<? extends OutputField> outputFields = evaluator.getOutputFields();
        System.out.println("*** Output fields: " + outputFields);


        //从数据源读取记录
        Map<String, Object> inputRecord = new LinkedHashMap<>();
        inputRecord.put("a","1");

        //封装模型参数
        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();

        //逐个字段从数据源模式映射到PMML模式
        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();

            Object rawValue = inputRecord.get(inputName.getValue());

            //将用户提供的任意值转换为已知的良好PMML值
            FieldValue inputValue = inputField.prepare(rawValue);

            arguments.put(inputName, inputValue);
        }

        //使用已知的参数对模型进行评估
        Map<FieldName, ?> results = evaluator.evaluate(arguments);

        //从jpmll - evaluator运行时环境中解耦结果
        Map<String, ?> resultRecord = EvaluatorUtil.decodeAll(results);

        //将记录写入数据接收器
        System.out.println("分数：" + resultRecord);
        String tt = resultRecord.get("x(1.0)").toString();
        String tt2 = resultRecord.get("y(0.0)").toString();
        System.out.println(tt);
        System.out.println(tt2);

        for (TargetField targetField : targetFields) {
            FieldName targetName = targetField.getName();
            Object targetValue = results.get(targetName);
            System.out.println(targetValue);
        }

        //使模型评估器有资格进行垃圾回收
//        evaluator = null;
    }

}