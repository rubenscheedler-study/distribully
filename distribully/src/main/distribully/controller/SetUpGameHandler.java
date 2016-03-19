package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import distribully.view.DistribullyWindow;

public class SetUpGameHandler implements ActionListener {

	private DistribullyWindow frame;
	
	public SetUpGameHandler(DistribullyWindow frame) {
		this.frame = frame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("setting up a game...");
		
		//no longer available for invitations
		if (DistribullyController.waitForInviteThread != null) {
			DistribullyController.waitForInviteThread.closeServer();
		}
		frame.getModel().getMe().setAvailable(false);
		frame.getModel().setGAME_STATE(GameState.INVITING_USERS);
		
		//create a game list on the server
		frame.getModel().getGamePlayerList().createGameList(frame.getModel().getMe());
		
		
		
		
	}

}
