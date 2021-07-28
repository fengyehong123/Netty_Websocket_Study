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
 * �����Զ����������
 * SimpleChannelInboundHandler:������������,�൱��[��վ,�뾳]
 */
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		
		// ͨ�������Ķ�������ȡchannel
		Channel channel = ctx.channel();
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
