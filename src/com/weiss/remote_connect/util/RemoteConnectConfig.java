package com.weiss.remote_connect.util;

import java.awt.Font;
import java.util.prefs.Preferences;

import com.weiss.remote_connect.client.RemoteClient;

public class RemoteConnectConfig {

	public static final int PORT = 50050;
	private static final Font baseFont = new Font("Arial Unicode", Font.PLAIN, 12);

	private static Preferences prefs = Preferences.userNodeForPackage(RemoteClient.class);

	public static void set(final String key, final String value) {
		prefs.put(key, value);
	}

	public static String get(final String key) {
		return get(key, "");
	}

	public static String get(final String key, final String def) {
		return prefs.get(key, def);
	}

	public static final Font getFont() {
		return baseFont;
	}

	public static final Font getFont(final float size) {
		return baseFont.deriveFont(size);
	}

	public static final Font getFont(final int style) {
		return baseFont.deriveFont(style);
	}

	public static final Font getFont(final int style, final float size) {
		return baseFont.deriveFont(style, size);
	}
}
