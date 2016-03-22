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
		//kill the lobby thread
		DistribullyController.lobbyThread.setInLobby(false);
		//go back to main page
		new BackToMainPageHandler(model);

		
	}

}
