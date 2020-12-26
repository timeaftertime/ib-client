package cn.milai.ib.mode.client.socket;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import cn.milai.ib.mode.client.Network.Message;
import cn.milai.ib.mode.client.Network.Message.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SocketHandler extends ChannelInboundHandlerAdapter {

	private final String TOKEN = "token";
	private String token;

	public SocketHandler(String token) {
		this.token = token;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Message message = Message.newBuilder()
			.setType(MessageType.LOGIN)
			.setData(JSONObject.toJSONString(Maps.immutableEntry(TOKEN, token)))
			.build();
		ctx.writeAndFlush(message);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//TODO 处理消息
	}

}
