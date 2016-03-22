
package distribully.view;


import javax.swing.JMenuItem;

import distribully.controller.GameState;
import distribully.controller.SetUpGameHandler;
import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;

public class SetUpGameButton extends JMenuItem{

	private static final long serialVersionUID = -3732106117892923335L;

	DistribullyModel model;
	
	public SetUpGameButton(DistribullyModel model) {
		this.model = model;
		this.setText("Set up game");
		
		this.addActionListener(new SetUpGameHandler(model));
		
	}

}

