package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import distribully.view.DistribullyWindow;

public class StartGameHandler implements ActionListener {

	private DistribullyWindow frame;
	
	public StartGameHandler(DistribullyWindow frame) {
		this.frame = frame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("starting a game...");
		frame.getModel().setGAME_STATE(GameState.INVITING_USERS);
		new ClientListUpdateHandler(frame.getModel());
	}

}
