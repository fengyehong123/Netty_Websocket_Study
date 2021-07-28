package com.imooc.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 初始化器类,当Channel完成注册后,会执行里面相应的初始化方法
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		
		/**
		 * 通过SocketChannel来获取对应的管道
		 */
		ChannelPipeline pipeline = channel.pipeline();
		
		/**
		 * 通过管道来添加handler
		 * 第一个参数是给handler取名字
		 * 第二个参数是具体的handler
		 * Netty内置了一部分Handler,我们可以直接拿来使用
		 * 当请求到服务器端的时候,我们需要解码,通过HttpServerCodec助手类来完成解码
		 */
		pipeline.addLast("HttpServerCodec", new HttpServerCodec());
		
		/**
		 * 添加我们自定义的Handler
		 * 返回一个字符串给客户端
		 */
		pipeline.addLast("customHandler", new CustomHandler());
	}

}
