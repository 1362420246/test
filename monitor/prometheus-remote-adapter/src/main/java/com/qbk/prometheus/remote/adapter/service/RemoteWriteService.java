package com.qbk.prometheus.remote.adapter.service;

import com.arpnetworking.metrics.prometheus.Remote;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xerial.snappy.Snappy;

import java.io.IOException;

@Slf4j
@Service
public class RemoteWriteService   {

    public void resolveRequest(byte[] data ) throws InvalidProtocolBufferException {
        byte[] compressed;
        Remote.WriteRequest writeRequest = null;
        try {
            compressed = Snappy.uncompress(data);
            writeRequest = Remote.WriteRequest.parseFrom(compressed);
            /*
            timeseries {
              labels {
                name: "__name__"
                value: "node_memory_MemTotal_bytes"
              }
              labels {
                name: "instance"
                value: "20.1.121.131:9100"
              }
              labels {
                name: "job"
                value: "node-exporter"
              }
              samples {
                value: 3.3565007872E10
                timestamp: 1600702641866
              }
            }
             */
            log.info("info from Prometheus:{}", writeRequest);
        } catch (IOException e) {
            log.error("receive msg from Prometheus error", e);
        }
//        List<Types.TimeSeries> timeseriesList = writeRequest.getTimeseriesList();
//        for (Types.TimeSeries timeSeries:timeseriesList) {
//
//            List<Types.Label> labelsList = timeSeries.getLabelsList();
//           // List<Types.Sample> samplesList = timeSeries.getSamplesList();
//
//            for (int i = 0; i < timeSeries.getLabelsCount(); i++) {
//                Types.Label label = labelsList.get(i);
//                log.info(label.getName() + ":" + label.getValue());
//            }
//        }
    }

}
