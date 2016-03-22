package distribully.controller;

import distribully.model.DistribullyModel;

public class BackToMainPageHandler {


	public BackToMainPageHandler(DistribullyModel model) {

		//start wait for invite thread
		DistribullyController.waitForInviteThread = new WaitForInviteThread(model);
		//set available on server
		model.getMe().setAvailable(true);
		//update game state
		model.setGAME_STATE(GameState.NOT_PLAYING);
		//refresh the online player list
		new ClientListUpdateHandler(model);
	}
}
