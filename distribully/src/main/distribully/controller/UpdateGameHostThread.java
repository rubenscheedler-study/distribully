package distribully.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import distribully.model.DistribullyModel;

public class UpdateGameHostThread extends Thread {
	private volatile boolean isSettingUpGame = false;
	private DistribullyModel model;
	private static Logger logger;
	
	public UpdateGameHostThread(DistribullyModel model) {
		logger = LoggerFactory.getLogger("controller.UpdateGameHostThread");
		this.model = model;
		isSettingUpGame = true;
		this.start();
	}
	public void run() {
		logger.info("Starting update game host thread for playerstatus only...");
		while (isSettingUpGame) {
			model.getGamePlayerList().fillWithGamePlayers(model.getNickname());
			model.updateInviteStatesByListState(model.getGamePlayerList());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("GameHostThread interrupted during sleep!");
				e.printStackTrace();
			}			
		}
		logger.info("No longer inviting players.");
	}
	public void setIsSettingUpGame(boolean isSettingUpGame){
		this.isSettingUpGame = isSettingUpGame;
	}
	
}
