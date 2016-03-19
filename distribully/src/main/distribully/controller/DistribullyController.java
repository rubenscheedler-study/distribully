package distribully.controller;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import distribully.model.DistribullyModel;
import distribully.view.*;

public class DistribullyController {
	
	DistribullyWindow view;
	DistribullyModel model;
	public static WaitForInviteThread waitForInviteThread = null;
	public static LobbyThread lobbyThread = null;
	public static ArrayList<InviteThread> InviteThreadList = new ArrayList<InviteThread>(); 
	
	public DistribullyController() {
		model = new DistribullyModel();
		model.setGAME_STATE(GameState.NOT_PLAYING);
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
