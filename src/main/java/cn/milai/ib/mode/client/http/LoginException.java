package cn.milai.ib.mode.client.http;

import cn.milai.ib.mode.client.IBClientException;

/**
 * 登录时遇到的异常
 * @author milai
 * @date 2020.12.24
 */
public class LoginException extends IBClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginException(String msg, Object... args) {
		super("登录失败：" + msg, args);
	}
}
