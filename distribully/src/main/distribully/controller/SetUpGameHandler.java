package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import distribully.model.DistribullyModel;

public class SetUpGameHandler implements ActionListener {

	private DistribullyModel model;
	private static Logger logger;

	public SetUpGameHandler(DistribullyModel model) {
		logger = LoggerFactory.getLogger("controller.SetUpGameHandler");
		this.model = model;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		logger.info("setting up a game...");

		//no longer available for invitations
		if (DistribullyController.waitForInviteThread != null) {
			DistribullyController.waitForInviteThread.closeServer();
		}
		model.getMe().setAvailable(false);

		//set current host name
		model.setCurrentHostName(model.getNickname());


		//update the available player list
		new ClientListUpdateHandler(model);

		//create a game list on the server
		model.getGamePlayerList().createGameList(model.getMe());


		model.setGAME_STATE(GameState.INVITING_USERS);

		//start the thread that will handle the distributed game
		DistribullyController.consumerThread = new GameConsumerThread(model);
		
		//start a thread that keeps the player list invite status up to date
		DistribullyController.updateGameHostThread = new UpdateGameHostThread(model);
	}

}
