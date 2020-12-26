package cn.milai.ib.mode.client.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;

import cn.milai.common.api.Resp;

/**
 * HTTP 客户端持有者
 * @author milai
 * @date 2020.12.23
 */
public class Https {

	/**
	 * 服务器地址
	 */
	private static final String URL_PREFIX = "http://localhost";

	/**
	 * 登录的 url
	 */
	private static final String LOGIN = URL_PREFIX + "/user/login";

	/**
	 * 注册的 url
	 */
	private static final String REGISTER = URL_PREFIX + "/user/register";

	/**
	 * http client 实例
	 */
	private static final HttpClient CLIENT = HttpClientBuilder.create().build();

	/**
	 * 请求与响应编码
	 */
	private static final Charset CHARSET = Charsets.UTF_8;

	/**
	 * 使用指定用户名、密码登录，失败将抛出异常
	 * @param username
	 * @param password
	 * @return 登录成功后的 token
	 * @throws LoginException
	 */
	public static String login(String username, String password) throws LoginException {
		HttpPost req = new HttpPost(LOGIN);
		req.setEntity(userReqEntity(username, password));
		try {
			HttpResponse httpResp = CLIENT.execute(req);
			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new LoginException("response = %s", httpResp.toString());
			}
			Resp<String> resp = toResp(httpResp);
			if (!resp.isSuccess()) {
				throw new LoginException(resp.getDesc());
			}
			return resp.getData();
		} catch (IOException e) {
			throw new LoginException("网络异常， e = %s", e);
		}
	}

	/**
	 * 使用指定用户名、密码注册，若注册成功将自动登录，失败将抛出异常
	 * @param username
	 * @param password
	 * @return 登录成功后的 token
	 * @throws RegisterException
	 * @throws LoginException
	 */
	public static String register(String username, String password) throws RegisterException, LoginException {
		HttpPost req = new HttpPost(REGISTER);
		req.setEntity(userReqEntity(username, password));
		try {
			HttpResponse httpResp = CLIENT.execute(req);
			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RegisterException("response = %s", httpResp.toString());
			}
			Resp<Void> resp = toResp(httpResp);
			if (!resp.isSuccess()) {
				throw new RegisterException(resp.getDesc());
			}
		} catch (IOException e) {
			throw new RegisterException("网络异常， e = %s", e);
		}
		return login(username, password);
	}

	/**
	 * 用户登录/注册请求 {@link HttpEntity}
	 * @param username
	 * @param password
	 * @return
	 */
	private static HttpEntity userReqEntity(String username, String password) {
		return new UrlEncodedFormEntity(
			Arrays.asList(
				new BasicNameValuePair("username", username),
				new BasicNameValuePair("password", password)
			), CHARSET
		);
	}

	@SuppressWarnings("unchecked")
	private static <T> Resp<T> toResp(HttpResponse resp) throws ParseException, IOException {
		return JSONObject.parseObject(EntityUtils.toString(resp.getEntity(), CHARSET), Resp.class);
	}

}
