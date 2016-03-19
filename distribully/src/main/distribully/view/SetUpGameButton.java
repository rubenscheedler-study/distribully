
package distribully.view;


import javax.swing.JMenuItem;

import distribully.controller.SetUpGameHandler;

public class SetUpGameButton extends JMenuItem{

	private static final long serialVersionUID = -3732106117892923335L;

	DistribullyWindow container;
	
	public SetUpGameButton(DistribullyWindow container) {
		this.container = container;
		
		this.setText("Set up game");
		
		this.addActionListener(new SetUpGameHandler(container));
		
	}

}

