package cn.milai.ib.mode.client.ui.form;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.milai.common.base.Strings;
import cn.milai.ib.mode.client.Https;
import cn.milai.ib.mode.client.LoginException;

/**
 * 登录注册界面
 * @author milai
 * @date 2020.12.25
 */
public class LoginForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(LoginForm.class);

	private static final int WIDTH = 600;
	private static final int HEIGHT = 420;

	private static final int USERNAME_MAX_LEN = 16;
	private static final int PASSWORD_MAX_LEN = 16;

	private JLabel title;
	private JTextField jtfUsername;
	private JPasswordField jtfPassword;
	private JButton submit;
	private JLabel actionSwitch;

	private Action action;
	private Action login = new Action() {

		@Override
		public String title() {
			return "登录";
		}

		@Override
		public String submitText() {
			return "登  录";
		}

		public String submittingText() {
			return "登录中...";
		};

		@Override
		public String switchText() {
			return "<html><font color='black'><a href>没有账号？注册</a></font></html>";
		}

		@Override
		public void submit() {
			Https.login(username(), password());
		}

		@Override
		public void checkValidate() {
			LoginForm.this.checkValidate();
		}

	};

	private Action register = new Action() {

		@Override
		public String title() {
			return "注册";
		}

		@Override
		public String submitText() {
			return "注  册";
		}

		public String submittingText() {
			return "注册中...";
		};

		@Override
		public String switchText() {
			return "<html><font color='black'><a href>已有账号？登录</a></font></html>";
		}

		@Override
		public void submit() {
			Https.register(username(), password());
		}

		@Override
		public void checkValidate() {
			LoginForm.this.checkValidate();
		}
	};

	private String username() {
		return jtfUsername.getText();
	}

	private String password() {
		return new String(jtfPassword.getPassword());
	}

	public LoginForm() {
		init();
	}

	private void init() {
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		GridBagLayout layout = new GridBagLayout();

		title = new JLabel();
		title.setFont(new Font("微软雅黑", Font.BOLD, 30));
		JPanel usernamePane = new JPanel(new BorderLayout());
		{
			JLabel label = new JLabel("用户名");
			jtfUsername = new JTextField(12);
			Font font = new Font("楷体", Font.BOLD, 15);
			label.setFont(font);
			jtfUsername.setFont(font);
			usernamePane.add(label, BorderLayout.WEST);
			usernamePane.add(jtfUsername, BorderLayout.EAST);
		}
		JPanel passwordPane = new JPanel(new BorderLayout());
		{
			JLabel label = new JLabel("密  码");
			jtfPassword = new JPasswordField(12);
			jtfPassword.setEchoChar('*');
			Font font = new Font("楷体", Font.BOLD, 15);
			label.setFont(font);
			jtfPassword.setFont(font);
			passwordPane.add(label, BorderLayout.WEST);
			passwordPane.add(jtfPassword, BorderLayout.EAST);
		}
		JPanel buttonPane = new JPanel(new GridLayout(2, 1));
		{
			submit = new JButton();
			submit.addActionListener(this::submit);
			Font font = new Font("楷体", Font.BOLD, 15);
			submit.setFont(font);
			buttonPane.add(submit, BorderLayout.CENTER);
			actionSwitch = new JLabel();
			actionSwitch.setCursor(new Cursor(Cursor.HAND_CURSOR));
			actionSwitch.setHorizontalAlignment(SwingConstants.CENTER);
			actionSwitch.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					switchToAction(action == login ? register : login);
				}
			});
			buttonPane.add(actionSwitch, BorderLayout.SOUTH);
		}

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		layout.addLayoutComponent(title, constraints);
		constraints.gridy = 1;
		layout.addLayoutComponent(usernamePane, constraints);
		constraints.gridy = 2;
		layout.addLayoutComponent(passwordPane, constraints);
		constraints.gridy = 3;
		layout.addLayoutComponent(buttonPane, constraints);

		add(title);
		add(usernamePane);
		add(passwordPane);
		add(buttonPane);
		setLayout(layout);

		switchToAction(login);
		setVisible(true);
	}

	private void switchToAction(Action act) {
		action = act;
		setTitle(action.title());
		title.setText(action.title());
		submit.setText(action.submitText());
		actionSwitch.setText(action.switchText());
	}

	private void submit(ActionEvent e) {
		try {
			action.checkValidate();
		} catch (LoginException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
			return;
		}
		submit.setEnabled(false);
		actionSwitch.setEnabled(false);
		submit.setText(action.submittingText());
		SwingUtilities.invokeLater(() -> {
			try {
				action.submit();
				dispose();
				SwingUtilities.invokeLater(() -> {
					new OnlineForm();
				});
			} catch (LoginException ex) {
				LOG.warn("username = {}, {}", username(), ExceptionUtils.getStackFrames(ex));
				JOptionPane.showMessageDialog(LoginForm.this, ex.getMessage());
			} finally {
				submit.setText(action.submitText());
			}
		});
		actionSwitch.setEnabled(true);
		submit.setEnabled(true);
	}

	public void checkValidate() {
		if (!Strings.lenRange(username(), 1, USERNAME_MAX_LEN)) {
			throw new LoginException("用户名长度必须为 1~%d", USERNAME_MAX_LEN);
		}
		if (!Strings.lenRange(password(), 1, PASSWORD_MAX_LEN)) {
			throw new LoginException("密码长度必须为 1~%d", PASSWORD_MAX_LEN);
		}
	}

	private interface Action {
		String title();

		String submitText();

		String submittingText();

		String switchText();

		void checkValidate() throws LoginException;

		void submit() throws LoginException;
	}

}
