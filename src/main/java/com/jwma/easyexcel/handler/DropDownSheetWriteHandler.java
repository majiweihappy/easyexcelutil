package com.jwma.easyexcel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.jwma.easyexcel.anno.ExplicitConstraint;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luolh
 * @version 1.0
 * @since 2020/6/2 11:00
 */
public class DropDownSheetWriteHandler implements SheetWriteHandler {

    private Class<?> clazz;
    /**
     * 表头占的行数（默认单行表头）
     */
    private Integer headSize = 1;

    /**
     * 下拉框生效的数据行数
     */
    private Integer size;

    /**
     * 列号-下拉数据集合
     */
    Map<Integer, String[]> explicitListConstraintMap = new HashMap<>();

    public DropDownSheetWriteHandler(Class<?> clazz, Integer headSize, Integer size) {
        this.clazz = clazz;
        this.headSize = headSize;
        this.size = size;
        parseBean(clazz);
    }

    public DropDownSheetWriteHandler(Class<?> clazz, Integer size) {
        this.clazz = clazz;
        this.size = size;
        parseBean(clazz);
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        //通过sheet处理下拉信息
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        explicitListConstraintMap.forEach((k, v) -> {
            CellRangeAddressList rangeList = new CellRangeAddressList();
            CellRangeAddress addr = new CellRangeAddress(headSize, headSize + size - 1, k, k);
            rangeList.addCellRangeAddress(addr);
            DataValidationConstraint constraint = helper.createExplicitListConstraint(v);
            DataValidation validation = helper.createValidation(constraint, rangeList);
            sheet.addValidationData(validation);
        });
    }

    private void parseBean(Class<?> clazz){
        Field[] declaredFields = clazz.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            //解析注解信息
            ExplicitConstraint explicitConstraint = field.getAnnotation(ExplicitConstraint.class);
          if(null != explicitConstraint){
              String[] explicitArray = explicitConstraint.dropDowns();
              if (explicitArray.length > 0) {
                  explicitListConstraintMap.put(i, explicitArray);
              }
          }
        }
    }
}
