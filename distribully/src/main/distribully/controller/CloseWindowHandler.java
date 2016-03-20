package distribully.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import distribully.model.DistribullyModel;

public class CloseWindowHandler extends WindowAdapter {
	
	DistribullyModel model;
	
	public CloseWindowHandler(DistribullyModel model){
		this.model = model;
	}
	@Override
    public void windowClosing(WindowEvent e)
    {
		model.getOnlinePlayerList().deleteFromServer(model.getNickname());
		if(model.getGAME_STATE() == GameState.IN_LOBBY){
			model.getGamePlayerList().deleteFromGame(model.getNickname(),model.getCurrentHostName());
		} else if (model.getGAME_STATE() == GameState.INVITING_USERS) {
			model.getGamePlayerList().deleteGameList(model.getNickname());//=current host name
		}
        System.out.println("Closed game");
        e.getWindow().dispose();
    }
}
