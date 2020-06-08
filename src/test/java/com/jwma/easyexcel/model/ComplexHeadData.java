package com.jwma.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author majiwei
 * @date 2020/6/5
 */
@Data
public class ComplexHeadData
{
    @ExcelProperty(value = {"主标题", "字符串标题"})
    private String string;

    @ExcelProperty(value = {"主标题", "日期标题"})
    private Date date;

    @ExcelProperty(value = {"主标题", "数字标题"})
    private Double doubleData;
}
