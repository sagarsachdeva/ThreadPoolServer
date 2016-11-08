package com.tcd.netapp.threadpoolserver;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServiceThread extends Thread {

	private Socket socket;
	private int status;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	private void setStatus() {
		status = 1;
	}

	private void setInputStream(Socket socket) {
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		setInputStream(this.socket);
		setStatus();
	}

	public int getStatus() {
		return status;
	}

	public ServiceThread() {
		this.status = 0;
	}

	public void run() {
		while (true) {
			try {
				sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (status == 0)
				continue;

			try {
				String input = (String) inputStream.readObject();

				if (input != null) {
					System.out.println("input from client : " + input);
					if (input.equals("HELO text\\n")) {
						String output = "HELO text\nIP:" + socket.getInetAddress() + "\nPort:" + socket.getPort()
								+ "\nStudentID:12345\n";
						outputStream.writeObject(output);
						outputStream.flush();
					}

					else if (input.equals("KILL_SERVICE\\n")) {
						outputStream.writeObject("Closing Service");
						outputStream.flush();
						System.exit(0);
						;
						continue;
					} else {
						continue;
					}
				}

			} catch (EOFException e) {
				killConnection();
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
	}

	private void killConnection() {
		// try {
		// socket.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		socket = null;
		inputStream = null;
		outputStream = null;
		status = 0;
	}

}
