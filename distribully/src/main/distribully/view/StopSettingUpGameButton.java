package distribully.view;

import javax.swing.JButton;

import distribully.controller.StopSettingUpGameHandler;
import distribully.model.DistribullyModel;

public class StopSettingUpGameButton extends JButton {

	private static final long serialVersionUID = -8838782574875415163L;
	
	public StopSettingUpGameButton(DistribullyModel model) {
		this.setText("Stop setting up game");
		this.addActionListener(new StopSettingUpGameHandler(model));
	}
}
