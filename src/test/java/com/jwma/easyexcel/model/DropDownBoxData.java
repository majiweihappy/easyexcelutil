package com.jwma.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jwma.easyexcel.anno.ExplicitConstraint;
import lombok.Data;

import java.util.Date;

/**
*  使用注解定义下拉框的数据
 * @author majiwei
 * @date 2020/6/5
 */
@Data
public class DropDownBoxData
{
    @ExcelProperty(value = {"主标题", "字符串标题"})
    @ExplicitConstraint(dropDowns = {"string1", "string2", "string3"})
    private String string;

    @ExcelProperty(value = {"主标题", "日期标题"})
    private Date date;

    @ExcelProperty(value = {"主标题", "数字标题"})
    private Double doubleData;
}
