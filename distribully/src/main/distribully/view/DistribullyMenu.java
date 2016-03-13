package distribully.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class DistribullyMenu extends JMenuBar {

	private static final long serialVersionUID = -5124367571594222448L;

	JMenu gameMenu;
	
	public DistribullyMenu() {
		gameMenu = new JMenu("Game");
		//1) start game button
		JMenuItem startGameItem = new StartGameButton();
		gameMenu.add(startGameItem);
		
		this.add(gameMenu);
		
	}
}
