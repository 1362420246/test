package com.qbk.nosql.hbase.demo.hbase;

import org.apache.hadoop.hbase.client.BufferedMutator;

/**
 * callback for hbase put delete and update
 *
 */
public interface MutatorCallback {
    
    /**
     * use mutator api to update put and delete
     *
     */
    void doInMutator(BufferedMutator mutator) throws Throwable;
}
