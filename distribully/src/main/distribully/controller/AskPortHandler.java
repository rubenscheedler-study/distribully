package distribully.controller;

import javax.swing.JOptionPane;

import distribully.model.DistribullyModel;

public class AskPortHandler {
	public AskPortHandler(DistribullyModel model){
			String portString = "";
			do {
				
				portString = JOptionPane.showInputDialog(null, "Please enter the port you want to use:");
				
				if (portString == null) { //User has cancelled, so stop the game
					DistribullyController.sendCloseEvent();
					return;
				} else {
					try{
						Integer.parseInt(portString);
					} catch (NumberFormatException e) { //Not a number
						portString = "";
						JOptionPane.showMessageDialog(null,
							    "Invalid port",
							    "Invalid port",
							    JOptionPane.WARNING_MESSAGE);
					}
					if(portString.equals(model.getServerPort())){ //Don't allow it, in the case you are running the server too.
						JOptionPane.showMessageDialog(null,
							    "This port is used by the server, choose a different port",
							    "Invalid port",
							    JOptionPane.WARNING_MESSAGE);
						portString = "";
					}
				}
				
			} while (portString.equals("")); //Keep asking till the user gives a proper port or stops.
			model.setMyPort(Integer.parseInt(portString));
	}
}
