package distribully.view;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class DistribullyMenu extends JMenuBar {

	private static final long serialVersionUID = -5124367571594222448L;

	public DistribullyMenu(DistribullyWindow container) {
		//gameMenu = new JMenu("Start Game");
		//1) start game button
		JMenuItem startGameItem = new SetUpGameButton(container);
		JMenuItem updateClientListButton = new UpdateClientListButton(container);
		this.add(startGameItem);
		this.add(updateClientListButton);
		
	}
}
