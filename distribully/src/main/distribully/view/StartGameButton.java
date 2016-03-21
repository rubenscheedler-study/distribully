package distribully.view;

import javax.swing.JButton;

import distribully.controller.StartGameHandler;
import distribully.model.DistribullyModel;

public class StartGameButton extends JButton {

	private static final long serialVersionUID = -3732106117892923335L;

	private DistribullyModel model;

	public StartGameButton(DistribullyModel model) {
		this.setText("Start Game");
		this.model = model;
		this.addActionListener(new StartGameHandler(model));
	}
}
