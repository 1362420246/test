package com.qbk.prometheus.remote.adapter.web;

import com.google.protobuf.InvalidProtocolBufferException;
import com.qbk.prometheus.remote.adapter.service.RemoteWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
public class RemoteWriteApi {

  @Autowired
  private RemoteWriteService remoteWriteService;

  /**
   * remote_write:
   *   - url: http://127.0.0.1:8090/write
   */
  @PostMapping("/write")
  public void write(@RequestBody byte[] data) throws InvalidProtocolBufferException {
    log.info(" receive msg from Prometheus ...");
    remoteWriteService.resolveRequest(data);
  }

  /**
   * protobuf 协议
   *
   * protobuf-java
   *
   * application/x-protobuf
   *
   * RequestMapping:
   *         value:  指定请求的实际地址， 比如 /action/info之类。
   *         method：  指定请求的method类型， GET、POST、PUT、DELETE等
   *         consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
   *         produces:    指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回
   *         params： 指定request中必须包含某些参数值是，才让该方法处理
   *         headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求
   */
  @RequestMapping(value="/write2" , method= RequestMethod.POST , consumes = "application/x-protobuf")
  public void write2(@RequestBody Map<String,Object> map) throws InvalidProtocolBufferException {
    System.out.println(map);
  }
}
