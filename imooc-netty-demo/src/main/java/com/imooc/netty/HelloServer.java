package com.imooc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *	ʵ�ֿͻ��˷���һ������,�������᷵��hello netty
 */
public class HelloServer {
	
	/**
	 * Netty�ٷ��Ƽ�ʹ�������߳�ģ�� 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		/**
		 * ����һ���߳��� (���߳���)
		 * ���߳������ڽ��տͻ��˵�����,���ǲ����κδ���,ֻ����������������߳���ȥ����
		 * ��Ϊ����ʹ�õ���NIO�߳�,����Ҫ����NIO�߳���
		 */
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		
		/**
		 * ����һ�Դ��߳���
		 * ���߳����������������߳���,�����߳���ȥ������������
		 */
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			/**
			 * Netty�������Ĵ���,ServerBootstrap��һ��������
			 */
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			/**
			 * �������߳������������,������̵߳�����ַ������������ദ��,�����߲�����Ҫ����
			 */
			serverBootstrap.group(bossGroup, workerGroup)
					/*
					 * ���ͻ��˺ͷ���������֮��,�����Channelͨ��,����ָ��ͨ��������ΪNioServerSocketChannel
					 * ��������NIO��˫��ͨ��
					 * */
					.channel(NioServerSocketChannel.class)
					// �Ӵ�����,���ڴ���workerGroup�е�����
					.childHandler(new HelloServerInitializer());
			/**
			 * ����Server,��������8088Ϊ�����Ķ˿ں�,ͬʱ������ʽΪͬ��
			 */
			ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
			/**
			 * �����ùرյ�channel,����Ϊͬ����ʽ
			 */
			channelFuture.channel().closeFuture().sync();
		} finally {
			// �ر��߳���
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
