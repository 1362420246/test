package com.qbk.nosql.hbase.demo.utils;

import com.google.common.collect.Maps;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;

/**
 * HBase工具
 */
@Component
public class HBaseUtil {

    @Autowired
    private Connection connection;

    private static Connection con;

    @PostConstruct
    private void init() throws Exception {
        if(this.connection == null ){
            throw new Exception() ;
        }
        con = connection ;
    }

    @PreDestroy
    public void dostory(){
        if (con != null) {
            try {
                con.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建表
     * @param tableName 表名
     * @param familyColumn 列簇
     */
    public static int createTable(String tableName, String... familyColumn) {
        Admin admin = null;
        try {
            admin = con.getAdmin();
            //判断表是否可用
            if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
                //添加列簇
                for (String familyName : familyColumn) {
                    tableDescriptor.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyName)).build());
                }
                //创建表
                admin.createTable(tableDescriptor.build());
                return 1;
            }else {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }finally {
            if(admin != null){
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除表
     */
    public static int dropTable(String tableName) {
        Admin admin = null;
        try {
            TableName tn = TableName.valueOf(tableName);
            admin = con.getAdmin();
            //判断表是否可用
            if(admin.isTableAvailable(tn)){
                admin = con.getAdmin();
                admin.disableTable(tn);
                admin.deleteTable(tn);
                return 1;
            }else {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }finally {
            if(admin != null){
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 插入或者更新 一个键值对
     * @param tableName 表名
     * @param rowKey    主键
     * @param family    列簇
     * @param qualifier 列
     * @param value     值
     */
    public static boolean insertOrUpdate(String tableName, String rowKey,String family, String qualifier, String value) {
        Table table = null ;
        try {
            table = con.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 插入多个键值对
     * @param tableName 表名
     * @param rowKey 主键
     * @param family 列簇
     * @param datas 键值对
     */
    public static boolean insertFamilyBatch(String tableName, String rowKey,String family,Map<String,String> datas){
        Table table = null ;
        try {
            table = con.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            datas.forEach(
                    (k,v) -> put.addColumn(Bytes.toBytes(family), Bytes.toBytes(k), Bytes.toBytes(v))
            );
            table.put(put);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 多行 单列族插入
     * @param tableName 表名
     * @param family 列簇
     * @param datas 多行 键值对
     */
    public static boolean insertBatch(String tableName, String family,Map<String,Map<String,String>> datas){
        Table table = null ;
        List<Put> puts = new ArrayList<>();
        try {
            table = con.getTable(TableName.valueOf(tableName));
            datas.forEach(
                    (rowKey,data)->{
                        if(!CollectionUtils.isEmpty(data)){
                            Put put = new Put(Bytes.toBytes(rowKey));
                            data.forEach(
                                    (k,v) -> put.addColumn(Bytes.toBytes(family), Bytes.toBytes(k), Bytes.toBytes(v))
                            );
                            puts.add(put);
                        }
                    }
            );
            table.put(puts);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除一行
     */
    public static boolean delRow(String tableName, String rowKey) {
        return delete(tableName, rowKey, null, null);
    }
    /**
     *  删除一行下的一个列族
     */
    public static boolean delFamily(String tableName, String rowKey, String family) {
        return delete(tableName, rowKey, family, null);
    }

    /**
     * 删除一行下的一个列族下的一个列
     */
    public static boolean delColumn(String tableName, String rowKey, String family, String qualifier) {
        return delete(tableName, rowKey, family, qualifier);
    }

    /**
     *  通用删除方法
     * @param tableName 表名
     * @param rowKey    行
     * @param family    列族
     * @param qualifier 列
     */
    public static boolean delete(String tableName, String rowKey, String family, String qualifier) {
        Table table = null ;
        try {
            table = con.getTable(TableName.valueOf(tableName));
            Delete del = new Delete(Bytes.toBytes(rowKey));
            if (qualifier != null) {
                del.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            } else if (family != null) {
                del.addFamily(Bytes.toBytes(family));
            }
            table.delete(del);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 批量删除行
     * @param tableName 表名
     * @param rowKeys 主键
     */
    public static boolean deleteBatch(String tableName, List<String> rowKeys){
        Table table = null ;
        try {
            table = con.getTable(TableName.valueOf(tableName));
            List<Delete> deletes = new ArrayList<>();
            rowKeys.forEach(
                    rowKey -> {
                        Delete delete = new Delete(Bytes.toBytes(rowKey));
                        deletes.add(delete);
                    }
            );
            table.delete(deletes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *  获取单列下的值
     * @param tableName 表名
     * @param rowKey    主键
     * @param family    列族
     * @param qualifier 列
     */
    public static String getValue(String tableName, String rowKey, String family, String qualifier) {
        Table table = null ;
        String value = null;
        try {
            table = con.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            Result result = table.get(get);
            List<Cell> ceList = result.listCells();
            if (ceList != null && ceList.size() > 0) {
                for (Cell cell : ceList) {
                    value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     *  获取 单行 单个列族 下的键值对
     * @param tableName 表名
     * @param rowKey    行
     * @param family    列族
     */
    public static Map<String, String> getFamilyValue(String tableName, String rowKey, String family) {
        Map<String, String> resultMap = new HashMap<>(16);
        Table table = null ;
        try {
            table = con.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes(family));
            Result result = table.get(get);
            List<Cell> ceList = result.listCells();
            if (ceList != null && ceList.size() > 0) {
                for (Cell cell : ceList) {
                    resultMap.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultMap;
    }


    /**
     *  取到 单行 多个列族 的键值对
     * @param tableName 表名
     * @param rowKey  主键
     */
    public static Map<String, Map<String, String>> getFamilyListValue(String tableName, String rowKey) {
        Map<String, Map<String, String>> results = new HashMap<>(16);
        Table table = null ;
        try {
            table = con.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            List<Cell> ceList = result.listCells();
            if (ceList != null && ceList.size() > 0) {
                for (Cell cell : ceList) {
                    String familyName = Bytes.toString(CellUtil.cloneFamily(cell));
                    results.computeIfAbsent(familyName, k -> new HashMap<>(16));
                    results.get(familyName).put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return results;
    }

    /**
     *  取到 多行
     * @param tableName 表名
     * @param rowKeys  主键
     */
    public static Map<String,Map<String,String>> getListValue(String tableName, List<String> rowKeys) {
        Map<String,Map<String,String>> rs = Maps.newLinkedHashMap();
        Table table = null ;
        List<Get> gets = new ArrayList<>();
        try {
            table = con.getTable(TableName.valueOf(tableName));
            rowKeys.forEach(
                    rowKey ->{
                        Get get = new Get(Bytes.toBytes(rowKey));
                        gets.add(get);
                    }
            );
            Result[] results = table.get(gets);
            rs = getResult(results);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs ;
    }

    /**
     * scan全表数据
     * @param tableName 表名
     */
    public static Map<String,Map<String,String>> getResultScann(String tableName) {
        Map<String,Map<String,String>> rs = Maps.newLinkedHashMap();
        Table table = null;
        ResultScanner resultScanner = null;
        try{
            TableName name = TableName.valueOf(tableName);
            table = con.getTable(name);
            //得到用于扫描 region 的对象
            Scan scan = new Scan();
            //使用 table 得到 resultcanner 实现类的对象
            resultScanner = table.getScanner(scan);
            rs = getResult(resultScanner);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(resultScanner != null){
                resultScanner.close();
            }
        }
        return rs;
    }

    /**
     * 扫描部分行
     * @param tableName 表名
     * @param rowPrefix row key 开头的前缀
     */
    public static Map<String,Map<String,String>> getValueByStartStopRowKey(String tableName, String rowPrefix ) {
        Table table = null;
        ResultScanner resultScanner = null;
        Map<String,Map<String,String>> rs = Maps.newLinkedHashMap();
        try {
            table = con.getTable(TableName.valueOf(tableName));
            //对表进行扫描,假设一个表填充了具有键“row1”，“row2”，“row3”的行，然后另一组是具有键“abc1”，“abc2”和“abc3”的行
            Scan scan = new Scan();
            //返回以“row”开头的行 ,  rowPrefix = "row"
            scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));
            resultScanner = table.getScanner(scan);
            rs = getResult(resultScanner);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(resultScanner != null){
                resultScanner.close();
            }
        }
        return rs;
    }

    /**
     * 获取结果集
     * TODO 忽略列族
     */
    private static Map<String,Map<String,String>> getResult(Result[] results){
        List<Result> list = Arrays.asList(results);
        Map<String,Map<String,String>> rowMap = Maps.newLinkedHashMap();
        list.forEach(
                result -> {
                    List<Cell> cells = result.listCells();
                    Map<String,String> kvMap = Maps.newLinkedHashMap();
                    cells.forEach(
                            cell -> {
                                //得到 rowkey
                                System.out.println("行键:" + Bytes.toString(CellUtil.cloneRow(cell)));
                                //得到列族
                                System.out.println("列族" + Bytes.toString(CellUtil.cloneFamily(cell)));

                                String k = Bytes.toString(CellUtil.cloneQualifier(cell));
                                String v = Bytes.toString(CellUtil.cloneValue(cell));
                                System.out.println("列:" + k);
                                System.out.println("值:" + v);

                                kvMap.put(k,v);
                            }
                    );
                    rowMap.put(Bytes.toString(result.getRow()),kvMap);
                }
        );
        return rowMap;
    }

    /**
     * 获取结果集
     * TODO 忽略列族
     */
    private static Map<String,Map<String,String>> getResult( ResultScanner resultScanner){
        Map<String,Map<String,String>> rowMap = Maps.newLinkedHashMap();
        resultScanner.forEach(
                result -> {
                    List<Cell> cells = result.listCells();
                    Map<String,String> kvMap = Maps.newLinkedHashMap();
                    cells.forEach(
                            cell -> {
                                //得到 rowkey
                                System.out.println("行键:" + Bytes.toString(CellUtil.cloneRow(cell)));
                                //得到列族
                                System.out.println("列族" + Bytes.toString(CellUtil.cloneFamily(cell)));

                                String k = Bytes.toString(CellUtil.cloneQualifier(cell));
                                String v = Bytes.toString(CellUtil.cloneValue(cell));
                                System.out.println("列:" + k);
                                System.out.println("值:" + v);

                                kvMap.put(k,v);
                            }
                    );
                    rowMap.put(Bytes.toString(result.getRow()),kvMap);
                }
        );
        return rowMap;
    }
}