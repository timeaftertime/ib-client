package cn.milai.ib.mode.client;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端全局变量持有类
 * @author milai
 * @date 2021.01.09
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Clients {

	private String token;
	private long userId;
	private ChannelHandlerContext ctx;

	/**
	 * 初始化客户端为指定用户信息
	 * @param userId
	 * @param token
	 */
	public void asUser(long userId, String token) {
		this.token = token;
		this.userId = userId;
	}

	/**
	 * 绑定 {@link this#ctx} 为参数指定
	 * @param ctx
	 */
	public void bindContext(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * 取消与当前 {@link this#ctx} 的绑定
	 */
	public void unbindContext() {
		this.ctx = null;
	}

	/**
	 * 获取客户端 token ，若未设置用户信息，返回 null
	 * @return
	 */
	public String token() {
		return token;
	}

	/**
	 * 获取客户端 userId ，若未设置用户信息，返回 0
	 * @return
	 */
	public long userId() {
		return userId;
	}

	/**
	 * 获取上一次绑定的 {@link ChannelHandlerContext}，若未绑定过，返回 null
	 * @return
	 */
	public ChannelHandlerContext channelHanlderContext() {
		return ctx;
	}
}
