package com.jwma.easyexcel.read;

import com.jwma.easyexcel.EasyExcelUtil;
import com.jwma.easyexcel.listener.DefaultExcelListener;
import com.jwma.easyexcel.model.ComplexHeadData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

/**
*  测试读文件
 * @author majiwei
 * @date 2020/6/5
 */
@RunWith(JUnit4.class)
public class ReadTest
{
    private final String FILE_ROOT = getClass().getResource("/").getPath();

    /**
     * 同步查询excel数据，返回原始的List<Map>>类型数据
     */
    @Test
    public void testSyncRead(){
        String path = FILE_ROOT + "test.xlsx";
        // 把2行表头一起查出来
        List<Map<Integer, String>> data = EasyExcelUtil.syncRead(path, 1, 0);
        Assert.assertEquals(6, data.size());

        // 只查数据，不查表头
        List<Map<Integer, String>> data1 = EasyExcelUtil.syncRead(path, 1, 2);
        Assert.assertEquals(4, data1.size());

        // 不传headRowNum，实际上是默认为1的，即默认为为单行表头，查询表头以下的所有数据
        List<Map<Integer, String>> data2 = EasyExcelUtil.syncRead(path, 1);
        Assert.assertEquals(5, data2.size());
    }

    @Test
    public void testSyncReadModel(){
        String path = FILE_ROOT + "test.xlsx";

        // 查询除标题行外的数据，这里表头有两行，headRowNum需要传2
        List<ComplexHeadData> data = EasyExcelUtil.syncReadModel(path, ComplexHeadData.class, 1, 2);
        Assert.assertEquals(4, data.size());

        // headRowNum不传默认为1，由于多读了一行表头，在做数据转换的时候会出错，所以返回的字段都是null
        // 这里返回4行数据，后面三行数据也为null，没找到原因
        List<ComplexHeadData> data1 = EasyExcelUtil.syncReadModel(path, ComplexHeadData.class, 1);
        Assert.assertEquals(5, data1.size());
    }

    /**
     *  异步读取，数据的操作都在listener中进行
     *  excel数据按行返回LinkedHashMap格式的数据
     * 可以按需要对数据进行存取
     */
    @Test
    public void testAsyncRead(){
        String path = FILE_ROOT + "test.xlsx";
        DefaultExcelListener listener = new DefaultExcelListener();
        // 把2行表头一起查出来
        EasyExcelUtil.asyncRead(path, listener, 1, 0);
        List rows = listener.getRows();
        Assert.assertEquals(6, rows.size());

        // 只查数据，不查表头
        listener = new DefaultExcelListener();
        EasyExcelUtil.asyncRead(path, listener, 1, 2);
        Assert.assertEquals(4, listener.getRows().size());

        // 不传headRowNum，实际上是默认为1的，即默认为为单行表头，查询表头以下的所有数据
        listener = new DefaultExcelListener();
        EasyExcelUtil.asyncRead(path, listener,1);
        Assert.assertEquals(5, listener.getRows().size());
    }

    /**
     *  异步读取，数据的操作都在listener中进行
     *  excel数据按行返回自定义格式的数据（数据格式转换出错会进入listener的onException方法进行处理）
     * 可以按需要对数据进行存取
     */
    @Test
    public void testAsyncReadModel(){
        String path = FILE_ROOT + "test.xlsx";
        DefaultExcelListener listener = new DefaultExcelListener();
        // 把2行表头一起查出来
        // 这里实际读取了6行数据，其中两行表头数据的日期列在数据格式转换的时候发生错误，直接跳过了，
        // 异常信息在listener中的onException方法进行处理
        EasyExcelUtil.asyncReadModel(path, listener, ComplexHeadData.class, 1, 0);
        List rows = listener.getRows();
        Assert.assertEquals(4, rows.size());

        // 只查数据，不查表头
        listener = new DefaultExcelListener();
        EasyExcelUtil.asyncReadModel(path, listener, ComplexHeadData.class, 1, 2);
        Assert.assertEquals(4, listener.getRows().size());

        // 不传headRowNum，实际上是默认为1的，即默认为为单行表头，查询表头以下的所有数据
        listener = new DefaultExcelListener();
        EasyExcelUtil.asyncReadModel(path, listener, ComplexHeadData.class,1);
        Assert.assertEquals(4, listener.getRows().size());
    }
}
