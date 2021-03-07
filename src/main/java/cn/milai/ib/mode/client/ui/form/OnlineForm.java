package cn.milai.ib.mode.client.ui.form;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.milai.ib.IBCore;
import cn.milai.ib.mode.client.IBClientException;
import cn.milai.nexus.NexusClient;

public class OnlineForm extends JFrame {

	private static final Logger LOG = LoggerFactory.getLogger(OnlineForm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 360;
	private static final int HEIGHT = 840;

	private NexusClient nexusClient = IBCore.getBean(NexusClient.class);

	public OnlineForm() {
		init();
	}

	private void init() {
		LOG.info("连接服务器...");
		try {
			nexusClient.connect().sync();
		} catch (InterruptedException e) {
			nexusClient.shutdown();
			throw new IBClientException(e);
		}
		LOG.info("成功连接到服务器");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				LOG.info("断开连接...");
				nexusClient.shutdown();
				LOG.info("成功断开连接");
			}
		});

		// TODO
		//		int lableNum = 0;
		//		JLabel[] labels = new JLabel[lableNum];
		//		for (int i = 0; i < lableNum; i++) {
		//			labels[i] = new JLabel("用户" + i);
		//			labels[i].setOpaque(true);
		//			Icon icon = new ImageIcon();
		//			labels[i].setIcon(icon);
		//		}
		//
		//		ListModel<JLabel> dataModel = new ResetableListModel<>(labels);
		//		JList<JLabel> jList = new JList<>(dataModel);
		//		jList.setCellRenderer(new MyCellRenderer());
		//		JScrollPane scrollPane = new JScrollPane(jList);
		//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//		this.add(scrollPane);

		setVisible(true);
	}

	@SuppressWarnings("unused")
	private class ResetableListModel<E> extends AbstractListModel<E> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Object[] element;

		public ResetableListModel(E[] element) {
			checkNull(element);
			this.element = element;
		}

		private void checkNull(E[] element) {
			if (element == null)
				throw new NullPointerException();
		}

		@Override
		public int getSize() { return element.length; }

		@SuppressWarnings("unchecked")
		@Override
		public E getElementAt(int index) {
			return (E) element[index];
		}
	}

	@SuppressWarnings("unused")
	private class MyCellRenderer implements ListCellRenderer<JLabel> {

		@Override
		public Component getListCellRendererComponent(JList<? extends JLabel> list, JLabel value, int index,
			boolean isSelected, boolean cellHasFocus) {
			Color background;
			Color foreground;
			// check if this cell represents the current DnD drop location
			if (isSelected) {
				background = Color.RED;
				foreground = Color.WHITE;
				// unselected, and not the DnD drop location
			} else {
				background = Color.WHITE;
				foreground = Color.BLACK;
			}
			value.setBackground(background);
			value.setForeground(foreground);
			return value;
		}

	}
}
