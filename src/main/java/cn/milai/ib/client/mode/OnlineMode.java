package cn.milai.ib.client.mode;

import org.springframework.stereotype.Component;

import cn.milai.ib.client.form.LoginForm;
import cn.milai.ib.mode.GameMode;

/**
 * 联网游戏模式
 * 2020.01.25
 * @author milai
 */
@Component
public class OnlineMode extends Thread implements GameMode {

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
