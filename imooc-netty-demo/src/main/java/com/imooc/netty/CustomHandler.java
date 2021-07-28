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
 * �����Զ����������
 * SimpleChannelInboundHandler:������������,�൱��[��վ,�뾳]
 */
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		
		// ͨ�������Ķ�������ȡchannel
		Channel channel = ctx.channel();
		
		// ֻ�е�msg��һ��Http�����ʱ��
		if (msg instanceof HttpRequest) {
			// ���Ի�ȡ�ͻ��˵�IP��ַ
			System.out.println(channel.remoteAddress());
			
			// ����������,���巢�͵�������Ϣ,������Ϣ��Ҫ���ڻ�������
			ByteBuf content = Unpooled.copiedBuffer("Hello netty~", CharsetUtil.UTF_8);
			
			/**
			 * ����һ��Http response
			 * ����1: HTTP�汾
			 * ����2: ״̬��
			 * ����3: ��Ӧ����
			 */
			DefaultFullHttpResponse response = 
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
			
			/**
			 * ������Ӧͷ
			 * ������Ӧͷ����������Ϊ�ı�(����������Ϊ�����Ƶ�)
			 */
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			
			/**
			 * ������Ӧͷ�ĳ���
			 */
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
			
			// ����Ӧˢ�µ��ͻ���
			ctx.writeAndFlush(response);
		}
	}
	
	/**
	 * ����ķ�������Channel���������ں���
	 */

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channelע����...");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel�Ƴ���...");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel���ڻ�Ծ״̬...");
		super.channelActive(ctx);
	}
	
	// ���ͻ��˺ͷ���˶Ͽ�����ʱ
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel���ڷǻ�Ծ״̬...");
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel��ȡ�������...");
		super.channelReadComplete(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("�û��¼�����...");
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel��д�¼�������...");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("Channel�����쳣...");
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("���������...");
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("�������Ƴ�...");
		super.handlerRemoved(ctx);
	}

}
