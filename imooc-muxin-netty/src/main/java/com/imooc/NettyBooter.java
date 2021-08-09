package com.imooc;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.imooc.netty.WSserver;

/**
 *我们定义一个类实现ApplicationListener接口
 *监听ContextRefreshedEvent事件，当所有的bean都初始化完成并被成功装载后会触发该事件,
 *实现ApplicationListener接口可以收到监听动作,然后可以写自己的逻辑.
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		// 如果上下文对象的父为空,就创建Netty服务器
		if (event.getApplicationContext().getParent() == null) {
			try {
				WSserver.getInstance().start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
