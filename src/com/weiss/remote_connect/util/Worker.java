package com.weiss.remote_connect.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Worker {

	private static ExecutorService exec = Executors.newCachedThreadPool();
	
	public static void execute(final Runnable r) {
		exec.execute(r);
	}
	
	public static Future<?> submit(final Runnable r) {
		return exec.submit(r);
	}
	
	public static <T> Future<T> submit(final Callable<T> c) {
		return exec.submit(c);
	}
	
}
