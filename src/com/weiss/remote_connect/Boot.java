package com.weiss.remote_connect;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.weiss.remote_connect.ui.RemoteConnectController;
import com.weiss.remote_connect.ui.RemoteConnectUI;

public class Boot {

	public static void main(String[] args) throws Exception {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				final RemoteConnectUI view = new RemoteConnectUI();
				new RemoteConnectController(view);
			}
		});
	}

}
