package com.qbk.nosql.hbase.demo.dao;

import com.qbk.nosql.hbase.demo.entity.CityDTO;
import com.qbk.nosql.hbase.demo.hbase.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class CityDTORowMapper implements RowMapper<CityDTO> {
    
    @Override
    public CityDTO mapRow(Result result, int rowNum){
        return CityDTO.builder()
                .rowkey(new String(result.getRow()))
                .name(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"))))
                .province(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("province"))))
                .code(new String(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("code"))))
                .build();
    }
    
}
