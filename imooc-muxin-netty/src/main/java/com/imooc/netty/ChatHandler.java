package com.imooc.netty;

import java.time.LocalDateTime;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * TextWebSocketFrame在Netty中,是用于为websocket专门处理文本的对象,frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	// 用于记录和管理所有客户端的Channel,固定的写法
	private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		
		// 获取从客户端发来的字符串
		String content = msg.text();
		System.out.println("从客户端接收到的消息为:" + content);
		
		// 当其中一个客户端发送消息给服务器之后,服务器会将该消息发送给所以连接到服务器的客户端
		for (Channel channel : clients) {
			/**
			 * writeAndFlush()的参数虽然是一个Object类型,但是我们不能直接把字符串放进去,而是应该放置一个TextWebSocketFrame对象
			 */
			channel.writeAndFlush(
					new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接收到消息,消息为:" + content)
			);
		}
		
		// 下面的这种方法和上面的for循环,作用是一致的
				// clients.writeAndFlush(new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接收到消息,消息为:" + content));
		
	}
	
	/**
	 * 当客户端连接到服务端之后(打开连接)
	 * 获取客户端的Channel,并且放到ChannelGroup中进行管理
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		
		// 当Handler加载的时候,获取到Channel对象
		Channel channel = ctx.channel();
		// 将Channel对象放到ChannelGroup中进行管理
		clients.add(channel);
	}

	/**
	 * 当用户断开连接或者刷新的时候,从ChannelGroup中移除Channel
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		
		// 当触发handlerRemoved的时候,ChannelGroup会自动移除对应的客户端channel,所以下面的代码其实是多余的
		// clients.remove(ctx.channel());
		
		/**
		 * 当Channel创建的时候,系统会给Channel创建两个id.一个长id,一个短id
		 */
		System.out.println("客户端断开,Channel对应的长ID为:" + ctx.channel().id().asLongText());
		System.out.println("客户端断开,Channel对应的短ID为:" + ctx.channel().id().asShortText());
	}

}
