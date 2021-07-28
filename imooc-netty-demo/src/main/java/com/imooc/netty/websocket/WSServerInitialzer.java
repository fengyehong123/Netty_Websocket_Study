package com.imooc.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitialzer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		
		// 获取管道
		ChannelPipeline pipeline = channel.pipeline();
		
		// 向管道中添加各种助手处理类Handler
		
		/**
		 * Netty提供的编解码器
		 * webcocket基于http协议,所以需要有http编解码器
		 */
		pipeline.addLast(new HttpServerCodec());
		
		// 对写大数据流的支持
		pipeline.addLast(new ChunkedWriteHandler());
		
	}
}
