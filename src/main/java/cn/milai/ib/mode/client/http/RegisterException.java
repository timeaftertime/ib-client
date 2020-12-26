package cn.milai.ib.mode.client.http;

import cn.milai.ib.mode.client.IBClientException;

/**
 * 注册时异常
 * @author milai
 * @date 2020.12.24
 */
public class RegisterException extends IBClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegisterException(Exception e) {
		super(e);
	}

	public RegisterException(String msg, Object... args) {
		super(msg, args);
	}

}
