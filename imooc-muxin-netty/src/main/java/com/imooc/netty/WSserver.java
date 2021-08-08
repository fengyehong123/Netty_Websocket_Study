package com.imooc.netty;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class WSserver {
	
	// 将WebSocket服务创建为单例形式
	private static class SingletionWSserver {
		static final WSserver instance = new WSserver();
	}
	
	// 获取WebSocket实例
	public static WSserver getInstance() {
		return SingletionWSserver.instance;
	}
	
	private NioEventLoopGroup mainGroup;
	private NioEventLoopGroup subGroup;
	private ServerBootstrap serverBootstrap;
	private ChannelFuture channelFuture;
	
	public WSserver() {
		
		// 定义一对主从线程
		mainGroup = new NioEventLoopGroup();
		subGroup = new NioEventLoopGroup();
		
		// 创建启动类
		serverBootstrap = new ServerBootstrap();
		// 主动线程放到启动类中,开启Channel,并且制定相应的助手处理类
		serverBootstrap.group(mainGroup, subGroup)
						.channel(NioServerSocketChannel.class)
						.childHandler(new WSServerInitialzer());
	}
	
	public void start() {
		
		// 启动Server,并且设置8088为启动的端口号
		this.channelFuture = serverBootstrap.bind(8088);
		// System.err.print => 设置打印在控制台上的字体是警告的红色字体
		System.err.print("netty websocket server 启动完毕...");
		
		/**
		 * 因为我们把Netty服务器放到了SpringBoot容器去管理,所以相关对象的关闭都由容器来管理
		 * 开发者就不需要关心了
		 */
	}
}
