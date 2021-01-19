package cn.milai.ib.mode.client;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;

import cn.milai.common.api.Resp;
import cn.milai.ib.IBCore;
import cn.milai.ibserver.service.dto.LoginResp;

/**
 * HTTP 工具类
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
	 * http client 实例
	 */
	private static final HttpClient CLIENT = HttpClientBuilder.create().build();

	/**
	 * 请求与响应编码
	 */
	private static final Charset CHARSET = Charsets.UTF_8;

	private static Clients clients = IBCore.getBean(Clients.class);

	/**
	 * 使用指定用户名、密码登录，失败将抛出异常
	 * @param username
	 * @param password
	 * @throws LoginException
	 */
	public static void login(String username, String password) throws LoginException {
		HttpPost req = new HttpPost(LOGIN);
		req.setEntity(userReqEntity(username, password));
		try {
			HttpResponse httpResp = CLIENT.execute(req);
			if (httpResp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new LoginException("response = %s", httpResp.toString());
			}
			Resp<LoginResp> resp = toResp(httpResp, LoginResp.class);
			if (!resp.isSuccess()) {
				throw new LoginException(resp.getDesc());
			}
			LoginResp data = resp.getData();
			clients.asUser(data.getUserId(), data.getToken());
		} catch (IOException e) {
			throw new LoginException("网络异常， e = %s", e);
		}
	}

	/**
	 * 使用指定用户名、密码注册，若注册成功将自动登录，失败将抛出异常
	 * @param username
	 * @param password
	 * @throws RegisterException
	 * @throws LoginException
	 */
	public static void register(String username, String password) throws LoginException {
		login(username, password);
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
	private static <T> Resp<T> toResp(HttpResponse resp, Class<? extends T> dataType) throws ParseException,
		IOException {
		Resp<JSONObject> r = JSON.parseObject(EntityUtils.toString(resp.getEntity(), CHARSET), Resp.class);
		return new Resp<T>(r.getCode(), r.getDesc(), r.getData().toJavaObject(dataType));
	}

}
