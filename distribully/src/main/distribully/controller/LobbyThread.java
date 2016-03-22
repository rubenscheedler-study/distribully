package distribully.controller;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import distribully.model.DistribullyModel;

public class LobbyThread extends Thread {
	private volatile boolean inLobby = false;
	private DistribullyModel model;
	private static Logger logger;

	public LobbyThread(DistribullyModel model) {
		this.model = model;
		logger = Logger.getLogger("controller.LobbyThread");
		logger.setParent(Logger.getLogger("controller.DistribullyController"));
		inLobby = true;
		this.start();
	}
	public void run() {
		logger.fine("Starting lobby...");
		while (inLobby) {
			if(model.getGamePlayerList().fillWithGamePlayers(model.getCurrentHostName())){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.warning("Lobbythread interrupted during sleep!");
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
		logger.fine("No longer in lobby.");
	}
	public void setInLobby(boolean inLobby){
		this.inLobby = inLobby;
	}

}
