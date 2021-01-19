package cn.milai.ib.mode.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.milai.ib.mode.client.Clients;
import cn.milai.ibserver.msg.AuthMsg;
import cn.milai.ibserver.msg.MsgCode;
import cn.milai.nexus.handler.Msg;
import cn.milai.nexus.handler.OnlineHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端连接生命周期管理器
 * @author milai
 * @date 2021.01.16
 */
@Component
public class ClientOnlineHandler implements OnlineHandler {

	private static Logger LOG = LoggerFactory.getLogger(ClientOnlineHandler.class);

	@Autowired
	private Clients clients;

	@Override
	public void connect(ChannelHandlerContext ctx) {
		Msg.writeAndFlush(ctx, MsgCode.AUTH, new AuthMsg(clients.userId(), clients.token()));
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx) {
		LOG.info("连接断开.....");
		// TODO 显示错误信息及断线重连
	}

}
