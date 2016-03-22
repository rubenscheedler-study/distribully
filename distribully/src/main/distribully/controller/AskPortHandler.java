package distribully.controller;

import javax.swing.JOptionPane;

import distribully.model.DistribullyModel;

public class AskPortHandler {
	public AskPortHandler(DistribullyModel model){
			String portString = "";
			do {
				
				portString = JOptionPane.showInputDialog(null, "Please enter the port you want to use:");
				
				if (portString == null) {
					DistribullyController.sendCloseEvent();
					return;
				} else {
					try{
						Integer.parseInt(portString);
					} catch (NumberFormatException e) {
						portString = "";
						JOptionPane.showMessageDialog(null,
							    "Invalid port",
							    "Invalid port",
							    JOptionPane.WARNING_MESSAGE);
					}
					if(portString.equals("4567")){
						JOptionPane.showMessageDialog(null,
							    "This port is used by the server, choose a different port",
							    "Invalid port",
							    JOptionPane.WARNING_MESSAGE);
						portString = "";
					}
				}
				
			} while (portString.equals(""));
			
			model.setMyPort(Integer.parseInt(portString));
	}
}
