package com.imooc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
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
		
		// 只有当msg是一个Http请求的时候
		if (msg instanceof HttpRequest) {
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
	
	/**
	 * 下面的方法都是Channel的生命周期函数
	 */

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel注册了...");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel移除了...");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel处于活跃状态...");
		super.channelActive(ctx);
	}
	
	// 当客户端和服务端断开连接时
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel处于非活跃状态...");
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel读取数据完毕...");
		super.channelReadComplete(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("用户事件触发...");
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel可写事件被更改...");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("Channel捕获到异常...");
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("助手类添加...");
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("助手类移除...");
		super.handlerRemoved(ctx);
	}

}
