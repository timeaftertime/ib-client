package cn.milai.ib.mode.client.socket;

/**
 * Socket 相关工具类
 * @author milai
 * @date 2020.12.26
 */
public class Sockets {

	private static final String HOST = "localhost";

	private static final int PORT = 8080;

	public static SocketClient newSocketClient(String token) {
		return new SocketClient(HOST, PORT, token);
	}

}
