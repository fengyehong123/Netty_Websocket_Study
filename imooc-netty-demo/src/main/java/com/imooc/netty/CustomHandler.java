package com.imooc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 创建自定义的助手类
 * SimpleChannelInboundHandler:对于请求来讲,相当于[入站,入境]
 */
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		
		// 通过上下文对象来获取channel
		Channel channel = ctx.channel();
		// 可以获取客户端的IP地址
		System.out.println(channel.remoteAddress());
		
		// 创建缓冲区,定义发送的数据消息,数据消息需要放在缓冲区中
		ByteBuf content = Unpooled.copiedBuffer("Hello netty~", CharsetUtil.UTF_8);
		
		/**
		 * 构建一个Http response
		 * 参数1: HTTP版本
		 * 参数2: 状态码
		 * 参数3: 响应内容
		 */
		DefaultFullHttpResponse response = 
				new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
		
		/**
		 * 设置响应头
		 * 设置响应头的内容类型为文本(还可以设置为二进制等)
		 */
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
		
		/**
		 * 设置响应头的长度
		 */
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
		
		// 将相应刷新到客户端
		ctx.writeAndFlush(response);
	}

}
