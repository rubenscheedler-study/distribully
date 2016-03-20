package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import distribully.model.DistribullyModel;

public class StopSettingUpGameHandler implements ActionListener {

	private DistribullyModel model;
	
	public StopSettingUpGameHandler(DistribullyModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//delete game list from server
		model.getGamePlayerList().deleteGameList(model.getCurrentHostName());
		//stop update thread of game list
		DistribullyController.updateGameHostThread.setIsSettingUpGame(false);
		//start wait for invite thread
		DistribullyController.waitForInviteThread = new WaitForInviteThread(model);
		//set available on server
		model.getMe().setAvailable(true);
		//update game state
		model.setGAME_STATE(GameState.NOT_PLAYING);
	}

}
