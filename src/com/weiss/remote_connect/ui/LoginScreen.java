package com.weiss.remote_connect.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.weiss.remote_connect.ui.wrappers.FlowPanel;
import com.weiss.remote_connect.ui.wrappers.VerticalFlowPanel;
import com.weiss.remote_connect.util.RemoteConnectConfig;

public class LoginScreen extends JPanel {

	public JPanel glassPane;
	public String localIp = "N/A";
	public String externalIp = "N/A";

	public JButton startServer;
	public JButton startClient;

	public JPanel loadingPanel;
	public JPanel serverAlreadyStartedPanel;
	public JPanel serverStartedSuccessfullyPanel;
	public JPanel clientConnectFailedPanel;
	public JPanel invalidIpPanel;
	public JLabel clientConnectFailedLabel;

	public JTextField ipField;

	public JPanel enterClientInfoPanel;

	public JLabel backButton;
	public JLabel loginButton;

	public JLabel connectingToServer;
	public JLabel loadingGif;
	private Image[] loadingSprites;

	public LoginScreen() {
		super(new GridBagLayout());
		this.init();
	}

	public void initGlassPane() {
		final int centerX = this.getPreferredSize().width / 2;
		final Rectangle glassPaneBounds = new Rectangle();

		glassPaneBounds.setLocation(centerX - (loadingPanel.getPreferredSize().width / 2), 
				this.startServer.getY() + this.startServer.getPreferredSize().height + 5);
		glassPaneBounds.setSize(this.loadingPanel.getPreferredSize());
		this.loadingPanel.setBounds(glassPaneBounds);

		glassPaneBounds.setLocation(centerX - (this.serverAlreadyStartedPanel.getPreferredSize().width / 2), 
				this.startServer.getY() + this.startServer.getPreferredSize().height + 5);
		glassPaneBounds.setSize(this.serverAlreadyStartedPanel.getPreferredSize());
		this.serverAlreadyStartedPanel.setBounds(glassPaneBounds);

		glassPaneBounds.setLocation(centerX - (this.serverStartedSuccessfullyPanel.getPreferredSize().width / 2), 
				this.startServer.getY() + this.startServer.getPreferredSize().height + 5);
		glassPaneBounds.setSize(this.serverStartedSuccessfullyPanel.getPreferredSize());
		this.serverStartedSuccessfullyPanel.setBounds(glassPaneBounds);

		glassPaneBounds.setLocation(centerX - (this.clientConnectFailedPanel.getPreferredSize().width / 2), 
				this.startServer.getY() + this.startServer.getPreferredSize().height + 5);
		glassPaneBounds.setSize(this.clientConnectFailedPanel.getPreferredSize());
		this.clientConnectFailedPanel.setBounds(glassPaneBounds);

		glassPaneBounds.setLocation(centerX - (this.enterClientInfoPanel.getPreferredSize().width / 2), 
				this.startServer.getY() + this.startServer.getPreferredSize().height + 5);
		glassPaneBounds.setSize(this.enterClientInfoPanel.getPreferredSize());
		this.enterClientInfoPanel.setBounds(glassPaneBounds);

	}


	private void init() {
		this.setPreferredSize(new Dimension(500, 400));

		this.glassPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.setColor(Color.LIGHT_GRAY);
				g.drawString("Local IP Address: " + localIp, 10, 20);
				g.drawString("External IP Address: " + externalIp, 10, 35);

			}
		};
		this.glassPane.setLayout(null);
		this.glassPane.setOpaque(false);

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1D;

		this.startServer = new JButton("Start Server");
		this.startServer.setFocusPainted(false);
		this.startServer.setFont(RemoteConnectConfig.getFont(15f));

		this.startClient = new JButton("Start Client");
		this.startClient.setFocusPainted(false);
		this.startClient.setFont(RemoteConnectConfig.getFont(15f));

		gbc.insets = new Insets(0, 3, 0, 3);

		gbc.anchor = GridBagConstraints.EAST;
		this.add(this.startServer, gbc);
		gbc.gridx++;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(this.startClient, gbc);

		this.loadingGif = new JLabel(new ImageIcon((this.loadingSprites = loadLoadingSprites())[0]));

		this.connectingToServer = new JLabel("Connecting to server") {

			private ScheduledExecutorService exec;
			private int index;

			@Override
			public void setVisible(boolean visible) {
				super.setVisible(visible);
				if (LoginScreen.this.loadingGif != null) {
					LoginScreen.this.loadingGif.setVisible(visible);

					if (visible) {
						if (exec != null && !exec.isShutdown()) {
							exec.shutdown();
						}
						index = 0;
						exec = Executors.newScheduledThreadPool(1);
						exec.scheduleWithFixedDelay(new Runnable() {
							@Override
							public void run() {
								if (index >=loadingSprites.length) {
									index = 0;
								}
								((ImageIcon) loadingGif.getIcon()).setImage(loadingSprites[index]);
								loadingGif.repaint();
								index++;
							};
						}, 0, 65, TimeUnit.MILLISECONDS);
					}
					else {
						exec.shutdown();
					}

				}
			}

		};
		this.connectingToServer.setFont(RemoteConnectConfig.getFont(18f));
		this.connectingToServer.setMinimumSize(this.connectingToServer.getPreferredSize());
		this.connectingToServer.setVisible(true);

		this.loadingPanel = new JPanel(new GridBagLayout());
		this.loadingPanel.setOpaque(false);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = gbc2.gridy = 0;
		gbc2.weightx = 1D;
		gbc2.weighty = 0D;
		gbc2.anchor = GridBagConstraints.NORTH;

		this.loadingPanel.add(Box.createVerticalStrut(15), gbc2);
		gbc2.gridy++;
		this.loadingPanel.add(this.connectingToServer, gbc2);
		gbc2.gridy++;
		this.loadingPanel.add(Box.createVerticalStrut(5), gbc2);
		gbc2.gridy++;
		gbc2.weighty = 1D;
		this.loadingPanel.add(this.loadingGif, gbc2);

		final JLabel serverAlreadyStartedLabel = new JLabel("A server is already bound to port " + RemoteConnectConfig.PORT);
		serverAlreadyStartedLabel.setFont(RemoteConnectConfig.getFont(Font.BOLD, 18f));
		serverAlreadyStartedLabel.setForeground(Color.red);

		this.serverAlreadyStartedPanel = new JPanel(new GridBagLayout());
		this.serverAlreadyStartedPanel.setOpaque(false);
		this.serverAlreadyStartedPanel.add(serverAlreadyStartedLabel);

		final JLabel serverStartedSuccessfullyLabel = new JLabel("<html><center>Server bound to<br />%IP%:" + RemoteConnectConfig.PORT + "</center></html>");
		serverStartedSuccessfullyLabel.setFont(RemoteConnectConfig.getFont(Font.BOLD, 18f));
		serverStartedSuccessfullyLabel.setForeground(Color.green.darker());

		try {
			serverStartedSuccessfullyLabel.setText(serverStartedSuccessfullyLabel.getText().replace("%IP%", InetAddress.getLocalHost().getHostAddress()));
		} catch (Exception e) {
			e.printStackTrace();
			serverStartedSuccessfullyLabel.setText(serverStartedSuccessfullyLabel.getText().replace("%IP%", ""));
		}

		this.clientConnectFailedLabel = new JLabel("<html><center>Failed to connect to server at<br />%IP%:" + RemoteConnectConfig.PORT + "</center></html>");
		this.clientConnectFailedLabel.setFont(RemoteConnectConfig.getFont(Font.BOLD, 18f));
		this.clientConnectFailedLabel.setForeground(Color.red);

		this.serverStartedSuccessfullyPanel = new JPanel(new GridBagLayout());
		this.serverStartedSuccessfullyPanel.setOpaque(false);
		this.serverStartedSuccessfullyPanel.add(serverStartedSuccessfullyLabel);

		this.clientConnectFailedPanel = new JPanel(new GridBagLayout());
		this.clientConnectFailedPanel.setOpaque(false);
		this.clientConnectFailedPanel.add(this.clientConnectFailedLabel);

		this.enterClientInfoPanel = new VerticalFlowPanel(FlowLayout.LEFT, 0) {
			@Override
			public void setVisible(boolean aFlag) {
				Point ipFieldLoc = ipField.getLocation();
				ipFieldLoc = SwingUtilities.convertPoint(enterClientInfoPanel, ipFieldLoc, glassPane);
				ipFieldLoc.translate(ipField.getWidth() + 5, ipField.getHeight()/4);

				invalidIpPanel.setLocation(ipFieldLoc);
				invalidIpPanel.setSize(invalidIpPanel.getPreferredSize());
				super.setVisible(aFlag);
			}
		};

		this.ipField = new JTextField(15);
		try {
			this.ipField.setText(RemoteConnectConfig.get("server_ip", InetAddress.getLocalHost().getHostAddress()));
		} catch (Exception ignored){
			ignored.printStackTrace();
		}

		this.enterClientInfoPanel.setOpaque(false);

		this.enterClientInfoPanel.add(new JLabel("Enter Server Address"));
		this.enterClientInfoPanel.add(this.ipField);

		try {
			this.loginButton = new JLabel(
					new ImageIcon(
							ImageIO.read(
									this.getClass().getResourceAsStream("/com/weiss/remote_connect/resources/login_button.png")
									).getScaledInstance(20, 20, Image.SCALE_SMOOTH)
							)
					);
		} catch (IOException e) {
			e.printStackTrace();
			this.loginButton = new JLabel("Login");
		}

		try {
			this.backButton = new JLabel(
					new ImageIcon(
							ImageIO.read(
									this.getClass().getResourceAsStream("/com/weiss/remote_connect/resources/login_back.png")
									).getScaledInstance(20, 20, Image.SCALE_SMOOTH)
							)
					);
		} catch (IOException e) {
			e.printStackTrace();
			this.backButton = new JLabel("Cancel");
		}

		this.loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.loginButton.setToolTipText("Login");

		this.backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.backButton.setToolTipText("Cancel");

		this.invalidIpPanel = new JPanel(new GridBagLayout());
		this.invalidIpPanel.add(new JLabel("Invalid IP Address")
		{{ setForeground(Color.red); }});

		this.glassPane.add(this.loadingPanel);
		this.glassPane.add(this.serverAlreadyStartedPanel);
		this.glassPane.add(this.serverStartedSuccessfullyPanel);
		this.glassPane.add(this.clientConnectFailedPanel);
		this.glassPane.add(this.enterClientInfoPanel);
		this.glassPane.add(this.invalidIpPanel);

		this.loadingPanel.setVisible(false);
		this.serverAlreadyStartedPanel.setVisible(false);
		this.serverStartedSuccessfullyPanel.setVisible(false);
		this.clientConnectFailedPanel.setVisible(false);
		this.enterClientInfoPanel.setVisible(false);
		this.invalidIpPanel.setVisible(false);

	}

	private Image[] loadLoadingSprites() {
		try {
			final BufferedImage sprite = ImageIO.read(LoginScreen.class.getResourceAsStream("/com/weiss/remote_connect/resources/loadingSprites.png"));
			final int width = sprite.getWidth();
			final int SPRITE_WIDTH = 128;
			if (width % SPRITE_WIDTH == 0) {
				final Image[] result = new Image[width / SPRITE_WIDTH];
				for (int i = 0; i < result.length; i++) {
					result[i] = sprite.getSubimage((i * SPRITE_WIDTH), 0, SPRITE_WIDTH, SPRITE_WIDTH).getScaledInstance(32, 32, Image.SCALE_SMOOTH);
				}
				return result;
			}
			else {
				System.err.println("Width % " + SPRITE_WIDTH + " != 0: " + width);
			}
			System.out.println("Loaded sprite file width : " + width);
		} catch (Exception e) {
			System.err.println("Error reading loading_sprites.png");
		}
		return new BufferedImage[] { };
	}

}
