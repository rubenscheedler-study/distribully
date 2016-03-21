package distribully.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

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
		}else if (model.getGAME_STATE() == GameState.IN_GAME || model.getGAME_STATE() == GameState.SETTING_RULES){
			int leaveGame = JOptionPane.showConfirmDialog (null, 
					"You are in the middle of a game, are you sure you want to quit?", 
					"Confirm",JOptionPane.YES_NO_OPTION); 
			if(leaveGame == JOptionPane.YES_OPTION){
				new LeaveGameHandler(model);
			}else{
				return;
			}
		}
        System.out.println("Closed game");
        System.exit(0);
    }
}
