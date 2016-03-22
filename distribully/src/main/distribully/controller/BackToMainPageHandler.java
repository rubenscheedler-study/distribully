package distribully.controller;

import distribully.model.DistribullyModel;

public class BackToMainPageHandler {
	public BackToMainPageHandler(DistribullyModel model) {
		//Start wait for invite thread
		DistribullyController.waitForInviteThread = new WaitForInviteThread(model);
		//Set available on server
		model.getMe().setAvailable(true);
		//Update game state
		model.setGAME_STATE(GameState.NOT_PLAYING);
		//Refresh the online player list
		new ClientListUpdateHandler(model);
	}
}
