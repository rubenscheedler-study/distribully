package distribully.controller;

import java.util.logging.Logger;

import distribully.model.DistribullyModel;

public class UpdateGameHostThread extends Thread {
	private volatile boolean isSettingUpGame = false;
	private DistribullyModel model;
	private static Logger logger;
	
	public UpdateGameHostThread(DistribullyModel model) {
		logger = Logger.getLogger("controller.UpdateGameHostThread");
		logger.setParent(Logger.getLogger("controller.DistribullyController"));
		this.model = model;
		isSettingUpGame = true;
		this.start();
	}
	public void run() {
		logger.fine("Starting update game host thread for playerstatus only...");
		while (isSettingUpGame) {
			model.getGamePlayerList().fillWithGamePlayers(model.getNickname());
			model.updateInviteStatesByListState(model.getGamePlayerList());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.warning("GameHostThread interrupted during sleep!");
				e.printStackTrace();
			}			
		}
		logger.fine("No longer inviting players.");
	}
	public void setIsSettingUpGame(boolean isSettingUpGame){
		this.isSettingUpGame = isSettingUpGame;
	}
	
}
