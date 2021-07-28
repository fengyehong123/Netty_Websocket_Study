package com.imooc.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WSserver {

	public static void main(String[] args) throws Exception {
		
		// ����һ�������߳�
		NioEventLoopGroup mainGroup = new NioEventLoopGroup();
		NioEventLoopGroup subGroup = new NioEventLoopGroup();
		
		try {
			// ����������
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			
			// �����̷߳ŵ���������,����Channel,�����ƶ���Ӧ�����ִ�����
			serverBootstrap.group(mainGroup, subGroup)
							.channel(NioServerSocketChannel.class)
							.childHandler(null);
		} finally {
			
			// �ر������߳�
			mainGroup.shutdownGracefully();
			subGroup.shutdownGracefully();
		}
	}
}
