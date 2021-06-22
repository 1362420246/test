package com.qbk.nosql.hbase.demo;

import com.qbk.nosql.hbase.demo.entity.CityDTO;
import com.qbk.nosql.hbase.demo.dao.CityDTORowMapper;
import com.qbk.nosql.hbase.demo.hbase.HbaseSystemException;
import com.qbk.nosql.hbase.demo.hbase.HbaseTemplate;
import com.qbk.nosql.hbase.demo.hbase.TableCallback;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * HBaseTemplate 测试
 */
@SpringBootTest(classes = HbaseDemoApplication.class)
public class HBaseTemplateTest {
    
    @Autowired
    private HbaseTemplate hbaseTemplate;
    
    /**
     * put data test
     */
    @Test
    public void testPut() {
        hbaseTemplate.execute("test", (TableCallback<Object>) table -> {
            String rowkey = "qbk1";
            Put put = new Put(Bytes.toBytes(rowkey));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes("guangzhou"));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("province"), Bytes.toBytes("guangdong"));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("code"), Bytes.toBytes("G-11"));
            table.put(put);
            return rowkey;
        });
    }
    
    /**
     * delete data test
     */
    @Test
    public void testDelete() {
        hbaseTemplate.execute("test", (TableCallback<Object>) table -> {
            String rowkey = "qbk1";
            Delete delete = new Delete(Bytes.toBytes(rowkey));
            table.delete(delete);
            return rowkey;
        });
    }

    /**
     * get data test
     */
    @Test
    public void testGet() {
        try {
            String rowkey = "qbk1";
            CityDTO results =
                    hbaseTemplate.get(
                            "test",
                            rowkey,
                            (result, rowNum) -> CityDTO.builder()
                                    .rowkey(new String(result.getRow()))
                                    .name(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"))))
                                    .province(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("province"))))
                                    .code(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("code"))))
                                    .build());
            System.out.println(results);
        } catch (HbaseSystemException e) {
            System.out.println("查询失败");
            e.printStackTrace();
        }
    }

    /**
     * scan data test
     */
    @Test
    public void testScan() {
        // [1-3]
        Scan scan = new Scan().withStartRow(Bytes.toBytes("qbk1")).withStopRow(Bytes.toBytes("qbk4"));

        List<CityDTO> results =
                hbaseTemplate.find(
                        "test",
                        scan,
                        (result, rowNum) ->
                                CityDTO.builder()
                                        .rowkey(new String(result.getRow()))
                                        .name(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"))))
                                        .province(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("province"))))
                                        .code(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("code"))))
                                        .build());
        results.forEach(System.out::println);
    }

    /**
     * scan filter data test
     */
    @Test
    public void testScanFilter() {
        Scan scan = new Scan().withStartRow(Bytes.toBytes("qbk1")).withStopRow(Bytes.toBytes("qbk4"));
        scan.addFamily(Bytes.toBytes("f1"));
        scan.setFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes("f1"),
                        Bytes.toBytes("code"),
                        CompareOperator.EQUAL,
                        Bytes.toBytes("G-11"))
        );

        List<CityDTO> results =
                hbaseTemplate.find(
                        "test",
                        scan,
                        (result, rowNum) ->
                                CityDTO.builder()
                                        .rowkey(new String(result.getRow()))
                                        .name(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"))))
                                        .province(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("province"))))
                                        .code(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("code"))))
                                        .build());
        results.forEach(System.out::println);
    }
    
    /**
     * get data test
     */
    @Test
    public void getTest() {
        try {
            CityDTO cityDTO = hbaseTemplate.get("test", "qbk1", new CityDTORowMapper());
            System.out.println(cityDTO);
        } catch (HbaseSystemException e) {
            System.out.println("查询失败");
            e.printStackTrace();
        }
    }
    
    /**
     * scan data test
     */
    @Test
    public void scanTest(){
        Scan scan = new Scan().withStartRow(Bytes.toBytes("qbk1")).withStopRow(Bytes.toBytes("qbk4"));
        List<CityDTO> list = hbaseTemplate.find("test", scan, new CityDTORowMapper());
        System.out.println(list);
    }
    
    
}
