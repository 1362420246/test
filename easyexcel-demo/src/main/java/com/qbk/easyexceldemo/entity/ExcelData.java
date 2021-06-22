package com.qbk.easyexceldemo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExcelData {

    /**
     * 名称
     */
    @NotNull(message = "不能为空")
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 序列号
     */
    @NotNull(message = "不能为空")
    @ExcelProperty(value = "序列号")
    @ContentStyle(dataFormat = 0x31)
    private String serialNumber;

}
