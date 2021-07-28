package com.imooc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *	实现客户端发送一个请求,服务器会返回hello netty
 */
public class HelloServer {
	
	/**
	 * Netty官方推荐使用主从线程模型 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		/**
		 * 定义一对线程组 (主线程组)
		 * 该线程组用于接收客户端的连接,但是不做任何处理,只负责把任务分配给从线程组去处理
		 * 因为我们使用的是NIO线程,所以要定义NIO线程组
		 */
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		
		/**
		 * 定义一对从线程组
		 * 主线程组会把任务分配给从线程组,当从线程组去处理具体的任务
		 */
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			/**
			 * Netty服务器的创建,ServerBootstrap是一个启动类
			 */
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			/**
			 * 将主从线程组放入启动类,具体的线程的任务分发处理由启动类处理,开发者并不需要关心
			 */
			serverBootstrap.group(bossGroup, workerGroup)
					/*
					 * 当客户端和服务器连接之后,会产生Channel通道,我们指定通道的类型为NioServerSocketChannel
					 * 我们设置NIO的双向通道
					 * */
					.channel(NioServerSocketChannel.class)
					// 子处理器,用于处理workerGroup中的任务
					.childHandler(new HelloServerInitializer());
			/**
			 * 启动Server,并且设置8088为启动的端口号,同时启动方式为同步
			 */
			ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
			/**
			 * 监听该关闭的channel,设置为同步方式
			 */
			channelFuture.channel().closeFuture().sync();
		} finally {
			// 关闭线程组
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
