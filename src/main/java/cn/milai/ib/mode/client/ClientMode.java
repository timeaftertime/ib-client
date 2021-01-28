package cn.milai.ib.mode.client;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.milai.ib.mode.GameMode;
import cn.milai.ib.mode.client.ui.form.LoginForm;
import cn.milai.nexus.EnableNexus;

/**
 * 联网游戏模式
 * 2020.01.25
 * @author milai
 */
@Order
@Component
@EnableNexus
public class ClientMode extends Thread implements GameMode {

	@Override
	public void run() {
		new LoginForm();
	}

	@Override
	public String name() {
		return "联机模式";
	}

	@Override
	public void init() {
	}
}
