package distribully.view;

import javax.swing.JButton;

import distribully.controller.LeaveLobbyHandler;
import distribully.model.DistribullyModel;

public class LeaveLobbyButton extends JButton {

	private static final long serialVersionUID = -7312096470633674182L;

	private DistribullyModel model;
	public LeaveLobbyButton(DistribullyModel model) {
		this.setText("Leave Lobby");
		
		this.addActionListener(new LeaveLobbyHandler(this.model));
	}
}
