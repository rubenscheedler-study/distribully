package distribully.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import distribully.model.DistribullyModel;

public class WaitForInviteThread extends Thread {
	private int port;
	private volatile boolean listen = false;
	private ServerSocket serverSocket;
	private DistribullyModel model;
	private static Logger logger;

	public WaitForInviteThread(DistribullyModel model) {
		this.model = model;
		listen = true;
		logger = LoggerFactory.getLogger("controller.WaitForInviteThread");
		this.start();
	}
	public void run() {
		this.port = model.getMyPort();
		try {
			serverSocket = new ServerSocket(port);
			logger.info("Server is listening for invites...");
			while (listen) {
				Socket clientSocket;
				try {
					clientSocket = serverSocket.accept();
					new Connection(clientSocket);
				} catch (SocketException e) {
					logger.error("SocketException in waitForInvite thread.");
					listen = false;
				}

			}
			logger.info("Stopped waiting for invites.");
			serverSocket.close();
		}
		catch (IOException ioException) {
			logger.error("Incorrect port");
			JOptionPane.showMessageDialog(null,
				    "Could not open port, choose a different port",
				    "Port bind error",
				    JOptionPane.ERROR_MESSAGE);
			new AskPortHandler(model);
			this.run();
		}
	}
	public void closeServer(){
		try{
			listen = false;
			if(serverSocket != null){
				serverSocket.close();
			}
		}catch(Exception e){
			//Will always throw exception if the thread is waiting for a response.
			logger.debug("WaitforInviteThread socket was interrupted");
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
				logger.error("Could not initiate datastreams.");
			}
		}
		private void handle() {
			try {
				String hostName = in.readUTF();

				//only when the user is not playing/setting up a game: show the received invite
				if (model.getGAME_STATE() == GameState.NOT_PLAYING) {
					int acceptedInvite = JOptionPane.showConfirmDialog (null, "You received a game invitation from " + hostName + ". Would you like to accept?", "Game Invitation",JOptionPane.YES_NO_OPTION);
					if (acceptedInvite == JOptionPane.YES_OPTION) {
						out.writeUTF("Accepted");

						//not available to invites anymore:
						model.getMe().setAvailable(false);
						model.setGAME_STATE(GameState.IN_LOBBY);
						model.setCurrentHostName(hostName);
						//start a thread that updates a list of game players
						DistribullyController.lobbyThread = new LobbyThread(model);
						DistribullyController.consumerThread = new GameConsumerThread(model);

						listen = false;//kill this thread
					} else {
						out.writeUTF("Rejected");
					}
				} else {
					out.writeUTF("Rejected");
				}
			} catch (IOException ioException) {
				logger.error("Could not handle invite.");
			} finally {
				try {
					clientSocket.close();
				} catch (IOException ioException) {
					logger.debug("Socket in waitForInviteThread interrupted");
				}
			}
		}
	}
}
