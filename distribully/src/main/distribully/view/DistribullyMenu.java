package distribully.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class DistribullyMenu extends JMenuBar {

	private static final long serialVersionUID = -5124367571594222448L;

	private DistribullyWindow container;
	private JMenu gameMenu;
	
	public DistribullyMenu(DistribullyWindow container) {
		//gameMenu = new JMenu("Start Game");
		//1) start game button
		this.container = container;
		JMenuItem startGameItem = new StartGameButton(container);
		JMenuItem joinClientListButton = new JoinClientListButton(container);
		
		this.add(startGameItem);
		this.add(joinClientListButton);
		
	}
}
