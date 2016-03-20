package distribully.controller;

import java.net.ServerSocket;

import distribully.model.DistribullyModel;

public class UpdateGameHostThread extends Thread {
	private volatile boolean isSettingUpGame = false;
	ServerSocket serverSocket;
	DistribullyModel model;
	String hostName;
	
	public UpdateGameHostThread(DistribullyModel model) {
		this.model = model;
		isSettingUpGame = true;
		this.start();
	}
	public void run() {
		System.out.println("Starting update game host thread for playerstatus only...");
		while (isSettingUpGame) {
			model.getGamePlayerList().fillWithGamePlayers(model.getNickname());
			model.updateInviteStatesByListState(model.getGamePlayerList());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("GameHostThread interrupted during sleep!");
				e.printStackTrace();
			}			
		}
		System.out.println("No longer inviting players.");
	}
	public void setIsSettingUpGame(boolean isSettingUpGame){
		this.isSettingUpGame = isSettingUpGame;
	}
	
}
