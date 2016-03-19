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
		//TODO: leave current game
        System.out.println("Closed game");
        e.getWindow().dispose();
    }
}
