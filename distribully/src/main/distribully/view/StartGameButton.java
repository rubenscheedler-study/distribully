package distribully.view;

import javax.swing.JButton;

import distribully.controller.StartGameHandler;
import distribully.model.DistribullyModel;

public class StartGameButton extends JButton {

	private static final long serialVersionUID = -3732106117892923335L;

	public StartGameButton(DistribullyModel model) {
		this.setText("Start Game");
		this.setEnabled(model.getGamePlayerList().getPlayers().size() > 1);
		this.addActionListener(new StartGameHandler(model));
	}
}
