package distribully.controller;


import javax.swing.JOptionPane;

import distribully.view.DistribullyWindow;

public class JoinClientListHandler {
	
	DistribullyWindow frame;
	public JoinClientListHandler(DistribullyWindow frame) {
		this.frame = frame;
		String choosenNickName = "";
		boolean nickNameIsUnique = false;
		do {
			choosenNickName = askUserForName();
			
			
		}
		while (choosenNickName != null || true);
		//check if name is unique
	}

	
	public String askUserForName() {
		return JOptionPane.showInputDialog(frame, "Please enter your nickname");
	}
	
	public boolean tryToClaimNickName(String nickname) {
		return true;
	}
}
