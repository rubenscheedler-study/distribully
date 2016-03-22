package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import distribully.model.DistribullyModel;

public class LeaveLobbyHandler implements ActionListener  {

	private DistribullyModel model;
	public LeaveLobbyHandler(DistribullyModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Delete me from the game list
		model.getGamePlayerList().deleteFromGame(model.getNickname(),model.getCurrentHostName());
		//Kill the lobby thread
		DistribullyController.lobbyThread.setInLobby(false);
		//Go back to main page
		new BackToMainPageHandler(model);
	}
}
