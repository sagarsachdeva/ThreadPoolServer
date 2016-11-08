package com.tcd.netapp.threadpoolserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestClient {
	public static void main(String[] args) {
		String server = args[0];
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		int port = Integer.parseInt(args[1]);
		Socket socket = null;
		try {
			socket = new Socket(server, port);
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			while (true) {
				outputStream.writeObject(consoleReader.readLine());
				outputStream.flush();
				String msg = (String) inputStream.readObject();
				System.out.println(msg);
//				inputStream.readObject();
			}
		}
//		catch(SocketTimeoutException e){
//			System.out.println("Waited for server message for 4 sec. Closing connection from my side");
//		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
