package cn.milai.ib.mode.client;

/**
 * ib-client 异常基类
 * @author milai
 * @date 2020.12.23
 */
public class IBClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IBClientException(Exception e) {
		super(e);
	}

	public IBClientException(String msg, Object... args) {
		super(String.format(msg, args));
	}

}
