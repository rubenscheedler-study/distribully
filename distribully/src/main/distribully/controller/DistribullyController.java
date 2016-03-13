package distribully.controller;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import distribully.model.DistribullyModel;
import distribully.view.*;

public class DistribullyController {
	
	DistribullyWindow view;
	DistribullyModel model;
	
	public DistribullyController() {
		model = new DistribullyModel();
		model.setGAME_STATE(GameState.INVITING_USERS);
		view = new DistribullyWindow(model);
		askUserForPort();
		new JoinClientListHandler(view);
		
	}
	
	public static void main(String[] args) {
		new DistribullyController();
	}
	

	
	public void askUserForPort() {
		String portString = "";
		do {
			
			portString = JOptionPane.showInputDialog(view, "Please enter the port you want to use:");
			
			if (portString == null) {
				view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
				return;
			} else {
				try{
					Integer.parseInt(portString);
				} catch (NumberFormatException e) {
					portString = "";
				}
			}
			
		} while (portString.equals(""));
		
		model.setMyPort(Integer.parseInt(portString));
		
	}
}
