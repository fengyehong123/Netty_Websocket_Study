package com.imooc.netty.websocket;

import java.time.LocalDateTime;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * TextWebSocketFrame��Netty��,������Ϊwebsocketר�Ŵ����ı��Ķ���,frame����Ϣ������
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	// ���ڼ�¼�͹������пͻ��˵�Channel,�̶���д��
	private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		
		// ��ȡ�ӿͻ��˷������ַ���
		String content = msg.text();
		System.out.println("�ӿͻ��˽��յ�����ϢΪ:" + content);
		
		// ������һ���ͻ��˷�����Ϣ��������֮��,�������Ὣ����Ϣ���͸��������ӵ��������Ŀͻ���
		for (Channel channel : clients) {
			/**
			 * writeAndFlush()�Ĳ�����Ȼ��һ��Object����,�������ǲ���ֱ�Ӱ��ַ����Ž�ȥ,����Ӧ�÷���һ��TextWebSocketFrame����
			 */
			channel.writeAndFlush(
				new TextWebSocketFrame("[��������]" + LocalDateTime.now() + "���յ���Ϣ,��ϢΪ:" + content)
			);
		}
		
		// ��������ַ����������forѭ��,������һ�µ�
		// clients.writeAndFlush(new TextWebSocketFrame("[��������]" + LocalDateTime.now() + "���յ���Ϣ,��ϢΪ:" + content));
		
	}
	
	/**
	 * ���ͻ������ӵ������֮��(������)
	 * ��ȡ�ͻ��˵�Channel,���ҷŵ�ChannelGroup�н��й���
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		
		// ��Handler���ص�ʱ��,��ȡ��Channel����
		Channel channel = ctx.channel();
		// ��Channel����ŵ�ChannelGroup�н��й���
		clients.add(channel);
	}

	/**
	 * ���û��Ͽ����ӻ���ˢ�µ�ʱ��,��ChannelGroup���Ƴ�Channel
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		
		// ������handlerRemoved��ʱ��,ChannelGroup���Զ��Ƴ���Ӧ�Ŀͻ���channel,��������Ĵ�����ʵ�Ƕ����
		// clients.remove(ctx.channel());
		
		/**
		 * ��Channel������ʱ��,ϵͳ���Channel��������id.һ����id,һ����id
		 */
		System.out.println("�ͻ��˶Ͽ�,Channel��Ӧ�ĳ�IDΪ:" + ctx.channel().id().asLongText());
		System.out.println("�ͻ��˶Ͽ�,Channel��Ӧ�Ķ�IDΪ:" + ctx.channel().id().asShortText());
	}

}
