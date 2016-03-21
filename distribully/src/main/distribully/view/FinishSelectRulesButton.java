package distribully.view;

import javax.swing.JButton;

import distribully.controller.FinishSelectRulesHandler;
import distribully.model.DistribullyModel;

public class FinishSelectRulesButton extends JButton {

	private static final long serialVersionUID = -8710485017306492138L;
	
	private DistribullyModel model;
	
	public FinishSelectRulesButton(DistribullyModel model) {
		this.model = model;
		this.setText("Finish");
		this.addActionListener(new FinishSelectRulesHandler(model));
	}
}
