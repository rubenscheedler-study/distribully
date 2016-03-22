package distribully.controller;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import distribully.model.DistribullyModel;

public class LobbyThread extends Thread {
	private volatile boolean inLobby = false;
	private DistribullyModel model;
	private static Logger logger;

	public LobbyThread(DistribullyModel model) {
		this.model = model;
		logger = LoggerFactory.getLogger("controller.LobbyThread");
		inLobby = true;
		this.start();
	}
	public void run() {
		logger.info("Starting lobby...");
		while (inLobby) {
			if(model.getGamePlayerList().fillWithGamePlayers(model.getCurrentHostName())){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error("Lobbythread interrupted during sleep!");
				}			
			}else{
				inLobby = false; //No players in this game (game has closed)
				new BackToMainPageHandler(model);
				JOptionPane.showMessageDialog(null,
					    "Lobby has been closed",
					    "Lobby closed",
					    JOptionPane.WARNING_MESSAGE);
			}
		}
		logger.info("No longer in lobby.");
	}
	public void setInLobby(boolean inLobby){
		this.inLobby = inLobby;
	}

}
