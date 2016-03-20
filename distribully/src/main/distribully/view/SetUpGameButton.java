
package distribully.view;


import javax.swing.JMenuItem;

import distribully.controller.GameState;
import distribully.controller.SetUpGameHandler;
import distribully.model.IObservable;
import distribully.model.IObserver;

public class SetUpGameButton extends JMenuItem implements IObserver{

	private static final long serialVersionUID = -3732106117892923335L;

	DistribullyWindow container;
	
	public SetUpGameButton(DistribullyWindow container) {
		this.container = container;
		container.getModel().addObserver(this);
		this.setText("Set up game");
		
		this.addActionListener(new SetUpGameHandler(container));
		
	}

	@Override
	public void update(IObservable observable) {
		this.setEnabled(container.getModel().getGAME_STATE() == GameState.NOT_PLAYING);
	}

}

