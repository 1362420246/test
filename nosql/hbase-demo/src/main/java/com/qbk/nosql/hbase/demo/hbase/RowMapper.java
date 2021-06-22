package com.qbk.nosql.hbase.demo.hbase;

import org.apache.hadoop.hbase.client.Result;

/**
 * Defines the requirements for an object that translates paths in
 * the tree into display rows.
 */
public interface RowMapper<T> {
    
    T mapRow(Result result, int rowNum) throws Exception;
}
