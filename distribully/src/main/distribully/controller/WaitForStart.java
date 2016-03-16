package distribully.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WaitForStart extends Thread {
	private int port;
	private boolean listen = false;
	public WaitForStart(int port) {
		this.port = port;
		listen = true;
		this.start();
	}
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server is listening for queue name...");
			serverSocket.setSoTimeout(1000);
			while (listen) {
				Socket clientSocket = serverSocket.accept();
				new Connection(clientSocket);
			}
			serverSocket.close();
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	public void setListen(boolean listen){
		this.listen = listen;
	}
	
	class Connection{
		DataInputStream in;
		DataOutputStream out;
		Socket clientSocket;
		public Connection(Socket aSocket) {
			try {
				clientSocket = aSocket;
				in = new DataInputStream(clientSocket.getInputStream());
				out = new DataOutputStream(clientSocket.getOutputStream());
				this.handle();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		private void handle() {
			try {
				String queueName = in.readUTF();
				System.out.println("client>" + queueName);
				//TODO: Check/update gamestate?
				String reversed = new StringBuffer(queueName).reverse().toString();
				out.writeUTF(reversed);
				listen = false;
			} catch (IOException ioException) {
				ioException.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}
	}
}
