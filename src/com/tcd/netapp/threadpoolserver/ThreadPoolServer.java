package com.tcd.netapp.threadpoolserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ThreadPoolServer {

	private static Map<Integer, ServiceThread> threadPool;

	public static void main(String args[]) {
		int poolSize = 3;
		populateThreadPool(poolSize);
		int port = Integer.parseInt(args[0]);
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);

			while (true) {
				Socket newSocket = serverSocket.accept();
				System.out.println("Client '" + newSocket.getInetAddress() + "' connected");

				ServiceThread serverThread = findFreeThread();

				if (serverThread == null) {
					continue;
				}
				serverThread.setSocket(newSocket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ServiceThread findFreeThread() {
		int i = 0;
		for (ServiceThread thread : threadPool.values()) {
			i++;
			if (thread.getStatus() == 0) {
				System.out.println("assigned thread : " + i);
				return thread;
			}
		}
		return null;
	}

	private static void populateThreadPool(int poolSize) {
		threadPool = new HashMap<Integer, ServiceThread>();
		for (int i = 1; i <= poolSize; i++) {
			ServiceThread t = new ServiceThread();
			t.start();
			threadPool.put(i, t);
		}
	}
}
