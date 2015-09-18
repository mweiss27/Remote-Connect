package com.weiss.remote_connect.ui;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.weiss.remote_connect.util.SwingUtil;

public class RemoteConnectUI extends JFrame {

	public final JPanel container;
	public final LoginScreen loginScreen;

	public static final String LOGIN_CARD = "login";
	public static final String CHAT_ROOM_CARD = "chat";

	public RemoteConnectUI() {
		super("Shenzai's Remote Desktop");

		this.container = new JPanel(new CardLayout());

		this.loginScreen = new LoginScreen();

		this.container.add(this.loginScreen, LOGIN_CARD);

		SwingUtil.getCardLayout(this.container).show(this.container, LOGIN_CARD);

		this.setContentPane(this.container);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);

		this.loginScreen.initGlassPane();
		this.setGlassPane(this.loginScreen.glassPane);
		this.loginScreen.glassPane.setVisible(true);

	}



}
