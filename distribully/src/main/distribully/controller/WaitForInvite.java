package distribully.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WaitForInvite extends Thread {
	private int port;
	private boolean listen = false;
	ServerSocket serverSocket;
	public WaitForInvite(int port) {
		this.port = port;
		listen = true;
		this.start();
	}
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server is listening for invites...");
			while (listen) {
				Socket clientSocket = serverSocket.accept();
				new Connection(clientSocket);
			}
			System.out.println("Job's done.");
			serverSocket.close();
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	public void setListen(boolean listen){
		this.listen = listen;
	}
	
	public void closeServer(){
		try{
			serverSocket.close();
		}catch(Exception e){
			//Will always throw exception. Ignore.
		}
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
				String hostName = in.readUTF();
				System.out.println("client>" + hostName); 
				
				//TODO: Check gamestate. If not waiting, return no.
				//TODO: show popUp
				
				
				
				String reversed = new StringBuffer(hostName).reverse().toString();
				out.writeUTF(reversed);
				if(reversed.equals("yes")){
					listen = false;
					//TODO: notify server of unavailability
					//TODO: Go to users overview of this game
				}
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
