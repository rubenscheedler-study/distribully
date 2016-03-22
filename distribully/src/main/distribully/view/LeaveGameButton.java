package distribully.view;

import javax.swing.JMenuItem;

import distribully.controller.LeaveGameHandler;
import distribully.model.DistribullyModel;

public class LeaveGameButton extends JMenuItem {

	private static final long serialVersionUID = -7660393393355352217L;

	private DistribullyModel model;
	public LeaveGameButton(DistribullyModel model) {
		this.model = model;
		
		this.setText("Leave Game");
		this.addActionListener(new LeaveGameHandler(model));
	}
}
