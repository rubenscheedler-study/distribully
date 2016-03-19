package distribully.controller;

import java.net.ServerSocket;

import distribully.model.DistribullyModel;

public class LobbyThread extends Thread {
	private volatile boolean inLobby = false;
	ServerSocket serverSocket;
	DistribullyModel model;

	public LobbyThread(DistribullyModel model) {
		this.model = model;
		inLobby = true;
		this.start();
	}
	public void run() {
		System.out.println("Starting lobby...");
		while (inLobby) {
			//TODO: Get and update game-PlayersList and view
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Lobbythread interrupted during sleep!");
				e.printStackTrace();
			}			
		}
		System.out.println("No longer in lobby.");
	}
	public void setInLobby(boolean inLobby){
		this.inLobby = inLobby;
	}
}
