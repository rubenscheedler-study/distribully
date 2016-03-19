package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import distribully.model.DistribullyModel;

public class LeaveLobbyHandler implements ActionListener  {

	DistribullyModel model;
	public LeaveLobbyHandler(DistribullyModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//delete me from the game list
		model.getGamePlayerList().deleteFromGame(model.getNickname(),model.getCurrentHostName());
		//set available for invites
		model.getMe().setAvailable(true);
		//kill the lobby thread
		//DistribullyController.lobbyThread.setInLobby(false);
		//change game state
		DistribullyController.waitForInviteThread = new WaitForInviteThread(model);
		//set game state
		model.setGAME_STATE(GameState.NOT_PLAYING);
		//refresh the online player list
		new ClientListUpdateHandler(model);
	}

}
