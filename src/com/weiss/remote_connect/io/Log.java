package com.weiss.remote_connect.io;

public class Log {

	public static void info(final String message) {
		synchronized(Log.class) {
			System.out.printf("[I] %s\n", message);
		}
	}

	public static void err(final String message) {
		synchronized(Log.class) {
			System.err.printf("[E] %s\n", message);
		}
	}

	public static void debug(final String message) {
		synchronized(Log.class) {
			System.out.printf("[D] %s\n", message);
		}
	}

	public static void main(String[] args) {
		info("Test info");
		err("Test err");
		debug("Test debug");
	}

}
