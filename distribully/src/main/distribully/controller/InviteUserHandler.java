package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import distribully.model.DistribullyModel;
import distribully.model.Player;

public class InviteUserHandler implements ActionListener {

	private DistribullyModel model;
	private String name;
	private static Logger logger;
	
	public InviteUserHandler(String username, DistribullyModel model) {
		this.model = model;
		name = username;
		logger = LoggerFactory.getLogger("controller.InviteUserHandler");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Player player = model.getOnlinePlayerList().getPlayers().stream().filter(x -> x.getName().equals(name)).findFirst().get();
		if(player == null){
			JOptionPane.showMessageDialog(null,
		    "Player no longer exists.",
		    "Error",
		    JOptionPane.ERROR_MESSAGE);
		}else{
			logger.info("creating invite thread...");
			InviteThread thread = new InviteThread(player, model); 
			DistribullyController.InviteThreadList.add(thread); //Invite the player. Thread this, since we don't want to wait
			model.putInviteState(name, "waiting for response...");
		}
		
	}

}
