package com.imooc.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
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
		
		/**
		 * 对http的message进行聚合,聚合成FullHttpRequest或者FullHttpResponse
		 * 几乎在netty中的编程,都会使用到此handler
		 */
		pipeline.addLast(new HttpObjectAggregator(1024 * 64));
		// =============================以上是用于支出HTTP协议的handler=================================
		
		/**
		 * websocket服务器的协议,用于指定给客户端连接访问的路由: /ws
		 * 此handler会帮我们处理一些繁重复杂的事情
		 * 会帮我们处理握手动作: handshaking(close, ping, pong) ping + pong = 心跳
		 * 对于websocket来讲,都是以frames进行传输的,不同的数据类型对应的frames也不同
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		
		/**
		 * 我们自定义的handler
		 */
		pipeline.addLast(new ChatHandler());
	}
}
