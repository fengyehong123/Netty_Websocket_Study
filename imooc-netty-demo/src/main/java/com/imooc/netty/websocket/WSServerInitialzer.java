package com.imooc.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitialzer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		
		// ��ȡ�ܵ�
		ChannelPipeline pipeline = channel.pipeline();
		
		// ��ܵ�����Ӹ������ִ�����Handler
		
		/**
		 * Netty�ṩ�ı������
		 * webcocket����httpЭ��,������Ҫ��http�������
		 */
		pipeline.addLast(new HttpServerCodec());
		
		// ��д����������֧��
		pipeline.addLast(new ChunkedWriteHandler());
		
	}
}
