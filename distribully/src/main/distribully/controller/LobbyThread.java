package distribully.controller;

import java.net.ServerSocket;

import javax.swing.JOptionPane;

import distribully.model.DistribullyModel;

public class LobbyThread extends Thread {
	private volatile boolean inLobby = false;
	ServerSocket serverSocket;
	DistribullyModel model;
	String hostName;

	public LobbyThread(DistribullyModel model) {
		this.model = model;
		inLobby = true;
		this.start();
	}
	public void run() {
		System.out.println("Starting lobby...");
		while (inLobby) {
			if(model.getGamePlayerList().fillWithGamePlayers(model.getCurrentHostName())){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Lobbythread interrupted during sleep!");
					e.printStackTrace();
				}			
			}else{
				inLobby = false;
				model.setGAME_STATE(GameState.NOT_PLAYING);
				DistribullyController.waitForInviteThread = new WaitForInviteThread(model);
				JOptionPane.showMessageDialog(null,
					    "Lobby has been closed",
					    "Lobby closed",
					    JOptionPane.WARNING_MESSAGE);
			}
		}
		System.out.println("No longer in lobby.");
	}
	public void setInLobby(boolean inLobby){
		this.inLobby = inLobby;
	}

}
