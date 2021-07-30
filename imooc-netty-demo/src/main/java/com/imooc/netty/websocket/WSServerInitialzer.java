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
		
		/**
		 * ��http��message���оۺ�,�ۺϳ�FullHttpRequest����FullHttpResponse
		 * ������netty�еı��,����ʹ�õ���handler
		 */
		pipeline.addLast(new HttpObjectAggregator(1024 * 64));
		// =============================����������֧��HTTPЭ���handler=================================
		
		/**
		 * websocket��������Э��,����ָ�����ͻ������ӷ��ʵ�·��: /ws
		 * ��handler������Ǵ���һЩ���ظ��ӵ�����
		 * ������Ǵ������ֶ���: handshaking(close, ping, pong) ping + pong = ����
		 * ����websocket����,������frames���д����,��ͬ���������Ͷ�Ӧ��framesҲ��ͬ
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		
		/**
		 * �����Զ����handler
		 */
		pipeline.addLast(new ChatHandler());
	}
}
