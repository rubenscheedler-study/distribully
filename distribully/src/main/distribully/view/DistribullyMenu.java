package distribully.view;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import distribully.controller.GameState;
import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;

public class DistribullyMenu extends JMenuBar implements IObserver {

	private static final long serialVersionUID = -5124367571594222448L;

	private DistribullyModel model;
	private JMenuItem startGameItem;
	private JMenuItem updateClientListButton;
	private JMenuItem leaveGameButton;
	public DistribullyMenu(DistribullyModel model) {
		this.model = model;
		this.model.addObserver(this);
		
		startGameItem = new SetUpGameButton(model);
		updateClientListButton = new UpdateClientListButton(model);
		leaveGameButton = new LeaveGameButton(model);
		
		toggleMenuItems(model.getGAME_STATE());
	}
	
	public void toggleMenuItems(GameState gameState) {
		this.removeAll();
		
		if (gameState == GameState.NOT_PLAYING) {
			this.add(startGameItem);
		} 
		if (gameState == GameState.NOT_PLAYING || gameState == GameState.INVITING_USERS) {
			this.add(updateClientListButton);
		}
		if (gameState == GameState.IN_GAME || gameState == GameState.SETTING_RULES || gameState == GameState.WAITING_FOR_GAMESTART) {
			this.add(leaveGameButton);
		}
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void update(IObservable observable, Object changedObject) {
		toggleMenuItems(model.getGAME_STATE());
	}
}
