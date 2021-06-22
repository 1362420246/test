
package com.qbk.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 事件监听
 *
 * 监听 ContextRefreshedEvent 事件
 */
@Component
public class ContextEventListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("初始化或刷新{@code ApplicationContext}时引发的事件。");
	}
}
