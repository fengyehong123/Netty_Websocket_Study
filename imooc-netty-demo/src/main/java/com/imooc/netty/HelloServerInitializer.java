package com.imooc.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * ��ʼ������,��Channel���ע���,��ִ��������Ӧ�ĳ�ʼ������
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		
		/**
		 * ͨ��SocketChannel����ȡ��Ӧ�Ĺܵ�
		 */
		ChannelPipeline pipeline = channel.pipeline();
		
		/**
		 * ͨ���ܵ������handler
		 * ��һ�������Ǹ�handlerȡ����
		 * �ڶ��������Ǿ����handler
		 * Netty������һ����Handler,���ǿ���ֱ������ʹ��
		 * �����󵽷������˵�ʱ��,������Ҫ����,ͨ��HttpServerCodec����������ɽ���
		 */
		pipeline.addLast("HttpServerCodec", new HttpServerCodec());
		
		/**
		 * ��������Զ����Handler
		 * ����һ���ַ������ͻ���
		 */
		pipeline.addLast("customHandler", new CustomHandler());
	}

}
