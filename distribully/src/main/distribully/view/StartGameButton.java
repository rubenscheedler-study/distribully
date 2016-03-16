
package distribully.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import distribully.controller.GameState;
import distribully.controller.StartGameHandler;

public class StartGameButton extends JMenuItem{

	private static final long serialVersionUID = -3732106117892923335L;

	DistribullyWindow container;
	
	public StartGameButton(DistribullyWindow container) {
		this.container = container;
		
		this.setText("start game");
		
		this.addActionListener(new StartGameHandler(container));
		
	}

}

