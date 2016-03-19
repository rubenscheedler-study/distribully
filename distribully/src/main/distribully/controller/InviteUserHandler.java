package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import distribully.model.DistribullyModel;
import distribully.model.Player;

public class InviteUserHandler implements ActionListener {

	DistribullyModel model;
	String name;
	public InviteUserHandler(String username, DistribullyModel model) {
		this.model = model;
		name = username;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Player player = model.getClientList().getPlayers().stream().filter(x -> x.getName().equals(name)).findFirst().get();
		if(player == null){
			//TODO: afvangen bitch
		}else{
			System.out.println("creating invite thread...");
			new InviteThread(player.getIp(),player.getPort(), model);
			model.putInviteState(name, "waiting for response...");
		}
		
	}

}
