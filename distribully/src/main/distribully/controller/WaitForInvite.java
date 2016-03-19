package distribully.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import distribully.model.DistribullyModel;

public class WaitForInvite extends Thread {
	private int port;
	private volatile boolean listen = false;
	ServerSocket serverSocket;
	DistribullyModel model;
	
	public WaitForInvite(DistribullyModel model) {
		this.model = model;
		this.port = model.getMyPort();
		listen = true;
		this.start();
	}
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server is listening for invites...");
			while (listen) {
				Socket clientSocket;
				try {
					clientSocket = serverSocket.accept();
					new Connection(clientSocket);
				} catch (SocketException e) {
					//TODO: ooit hier iets mee doen
				}
				
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
				
				//only when the user is not playing/setting up a game: show the received invite
				if (model.getGAME_STATE() == GameState.NOT_PLAYING) {
					int acceptedInvite = JOptionPane.showConfirmDialog (null, "You received a game invitation from " + hostName + ". Would you like to accept?", "Game Invitation",JOptionPane.YES_NO_OPTION);
					if (acceptedInvite == JOptionPane.YES_OPTION) {
						out.writeUTF("Accepted");
						
						model.getMe().setAvailable(false);
						model.setGAME_STATE(GameState.IN_LOBBY);

						listen = false;
					} else {
						out.writeUTF("Rejected");
					}
				} else {
					out.writeUTF("Rejected");
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
