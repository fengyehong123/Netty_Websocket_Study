package com.imooc.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WSserver {

	public static void main(String[] args) throws Exception {
		
		// 定义一对主从线程
		NioEventLoopGroup mainGroup = new NioEventLoopGroup();
		NioEventLoopGroup subGroup = new NioEventLoopGroup();
		
		try {
			// 创建启动类
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			
			// 主动线程放到启动类中,开启Channel,并且制定相应的助手处理类
			serverBootstrap.group(mainGroup, subGroup)
							.channel(NioServerSocketChannel.class)
							.childHandler(null);
		} finally {
			
			// 关闭主从线程
			mainGroup.shutdownGracefully();
			subGroup.shutdownGracefully();
		}
	}
}
