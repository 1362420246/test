package com.qbk.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * 异步接口
 */
@Slf4j
@RestController
public class AsyncController {

	@Autowired
	private AsyncService asyncService;

	/**
	 * DeferredResult 方式异步接口
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/order")
	public DeferredResult<String> order() throws Exception {
		log.info("主线程开始");
		//主线程随机生成订单号
		String orderNumber = RandomStringUtils.randomNumeric(8);
		log.info("主线程生成订单号："+orderNumber);
		DeferredResult<String> result = asyncService.getAsyncUpdate(orderNumber);
		log.info("主线程结束");
		return result;
	}

	/**
	 * Callable 方式异步接口
	 */
	@GetMapping("/callable")
	public Callable<String> callable(){
		log.info("主线程开始");
		Callable<String> result = new Callable<String>() {
			@Override
			public String call() throws Exception {
				log.info("副线程开始");
				Thread.sleep(1000);
				log.info("副线程返回");
				return "success";
			}
		};
		log.info("主线程结束");
		return result;
	}

}
