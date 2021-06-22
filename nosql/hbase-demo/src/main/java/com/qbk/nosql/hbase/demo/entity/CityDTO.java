package com.qbk.nosql.hbase.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityDTO {
    
    private String rowkey;
    private String name;
    private String province;
    private String code;

}
