package com.qbk.easyexceldemo.controller;

import com.alibaba.excel.EasyExcel;
import com.qbk.easyexceldemo.entity.ExcelData;
import com.qbk.easyexceldemo.listener.ExcelDataListener;
import com.qbk.easyexceldemo.service.ExeclDataService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 导入
 */
@RestController
public class ImportController {

    private final static List<String> EXCEL_EXTENSION = Arrays.asList("xls","xlsx");

    @Autowired
    private ExeclDataService execlDataService;

    /**
     * 导入平台摄像头
     */
    @PostMapping(value = "/import", consumes="multipart/form-data")
    public Object importData(MultipartFile file) throws Exception {
        if(Objects.isNull(file)){
           return "file不能为空" ;
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(!EXCEL_EXTENSION.contains(extension)){
            return "file只能是excel文件" ;
        }

        ExcelDataListener excelDataListener = new ExcelDataListener(execlDataService);
        EasyExcel.read(
                    file.getInputStream(),  //excel文件流
                    ExcelData.class,   //数据的对象类型
                    excelDataListener// Excel 监听器
            ).ignoreEmptyRow(false)  //是否 忽略空行
                    .sheet(0) //默认第0个 sheet
                    .headRowNumber(2) //表头几行
                    .doRead(); //读取数据

        //错误返回
        if(excelDataListener.errors.size() > 0){
            return excelDataListener.errors;
        }

        return "ok";
    }
}
