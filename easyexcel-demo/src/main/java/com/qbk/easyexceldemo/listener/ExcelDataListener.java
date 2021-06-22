package com.qbk.easyexceldemo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.qbk.easyexceldemo.entity.ExcelData;
import com.qbk.easyexceldemo.service.ExeclDataService;
import com.qbk.easyexceldemo.util.BeanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel Data Listener
 * Listener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 */
public class ExcelDataListener extends AnalysisEventListener<ExcelData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelDataListener.class);

    /**
     * 每隔n条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;

    /**
     * 存储插入的数据
     */
    private final List<ExcelData> list = new ArrayList<>();

    /**
     * 记录异常的数组
     */
    public final List<String> errors = new ArrayList<>();

    /**
     * 业务的 service
     */
    private final ExeclDataService execlDataService;

    public ExcelDataListener(ExeclDataService execlDataService ) {
        this.execlDataService = execlDataService;
    }

    /**
     * Excel文件请求头验证
     */
    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println(headMap);
        System.out.println(context);
    }

    /**
     * Excel文件请求头验证
     * 根据 headRowNumber 指定执行几次
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println(headMap);
        System.out.println(context);
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(ExcelData data, AnalysisContext context) {
        //校验
        String validateEntity = BeanValidator.validateEntity(data);
        if (!StringUtils.isEmpty(validateEntity)) {
            throw new RuntimeException("第" + context.readRowHolder().getRowIndex() + "行:" + validateEntity);
        }
        LOGGER.info("解析到一条数据:{}",data);
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            //判定是否有异常，无异常才插入数据
            if(errors.size()== 0){
                saveData();
                // 存储完成清理 list
                list.clear();
            }
        }
    }
    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if(errors.size() > 0 ){
            //有异常 不存储数据
            return;
        }
        if(list.size() == 0){
            //无插存储数据
           return;
        }

        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
    }
    /**
     * 加上存储数据库
     */
    private void saveData() {
        execlDataService.saveData(list);
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        LOGGER.error("解析失败:{}", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            String error = String.format("第%d行，第%d列解析异常,类型转换错误", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
            LOGGER.error(error);
            //throw new RuntimeException(error);

            //添加异常
            errors.add(error);
        }else {
            errors.add("异常:" +  exception.getMessage());
        }
        //throw exception;
    }

}
