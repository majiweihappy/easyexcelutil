package com.jwma.easyexcel.write;

import com.jwma.easyexcel.EasyExcelUtil;
import com.jwma.easyexcel.handler.DropDownSheetWriteHandler;
import com.jwma.easyexcel.model.ComplexHeadData;
import com.jwma.easyexcel.model.DropDownBoxData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
*  测试写文件
 * @author majiwei
 * @date 2020/6/5
 */
@RunWith(JUnit4.class)
public class WriteTest
{
    private final String FILE_ROOT = getClass().getResource("/").getPath();

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp(){
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();
    }

    /**
     * 测试：无模板写文件（表头和数据都使用最原始的格式）
     */
    @Test
    public void testWriteWithoutTemplate(){
        String path = FILE_ROOT + "test1.xlsx";
        List<List<String>> head = getHead();
        List<List<Object>> data = getData();

        EasyExcelUtil.write(path, head, data, 0, "sheetName");
    }

    /**
     * 测试：根据模板写文件（通过@ExcelProperty注解来指定复杂表头）
     */
    @Test
    public void testWriteWithTemplate(){
        String path = FILE_ROOT + "test2.xlsx";
        EasyExcelUtil.write(path, ComplexHeadData.class, data(), 0, "sheetName");
    }

    /**
     * 测试：写出多个sheet页的数据
     * 原始数据和模板数据可以混合使用
     */
    @Test
    public void testWriteWithMultiSheets(){
        String path = FILE_ROOT + "test3.xlsx";
        List<List<String>> head = getHead();
        List<List<Object>> data = getData();

        EasyExcelUtil.writeWithSheets(path)
                .writeModel(ComplexHeadData.class, data(),  "sheetName0")
                .write(head, data,"sheetName1")
                .finish();
    }

    /**
     * 测试：网页导出excel并下载
     * @throws IOException .
     */
    @Test
    public void testWriteWebWithMultiSheets() throws IOException
    {
        List<List<String>> head = getHead();
        List<List<Object>> data = getData();

        EasyExcelUtil.writeWithSheetsWeb(response, "exportFile.xlsx")
                .writeModel(ComplexHeadData.class, data(),  "sheetName0")
                .write(head, data,"sheetName1")
                .finish();
    }

    /**
     * 测试：使用自定义的 SheetWriterHandler 来实现单元格下拉
     * 1、定义模型类 DropDownBoxData
     * 2、在需要下拉的字段添加@ExplicitConstraint注解，设置dropDowns属性来指定下拉的内容
     * 3、创建一个DropDownSheetWriteHandler，指定模型类，表头所占行数（不写默认单行表头），下拉生效行数
     */
    @Test
    public void testWriteWithWriteHandler(){
        String path = FILE_ROOT + "test4.xlsx";
        EasyExcelUtil.write(path, DropDownBoxData.class, data1(), new DropDownSheetWriteHandler(DropDownBoxData.class, 2, data1().size()) , 0, "sheetName");
    }

    private List<ComplexHeadData> data() {
        List<ComplexHeadData> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ComplexHeadData data = new ComplexHeadData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    private List<DropDownBoxData> data1() {
        List<DropDownBoxData> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DropDownBoxData data = new DropDownBoxData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    private List<List<String>> getHead(){
        List<List<String>> head = new ArrayList<>();
        head.add(Arrays.asList("主标题","字符串标题"));
        head.add(Arrays.asList("主标题","日期标题"));
        head.add(Arrays.asList("主标题","数字标题"));
        return head;
    }

    private List<List<Object>> getData(){
        List<List<Object>> data = new ArrayList<>();
        data.add(Arrays.asList("字符串0", new Date(), 0.56));
        data.add(Arrays.asList("字符串1", new Date(), 0.56));
        data.add(Arrays.asList("字符串2", new Date(), 0.56));
        data.add(Arrays.asList("字符串3", new Date(), 0.56));
        return data;
    }
}
