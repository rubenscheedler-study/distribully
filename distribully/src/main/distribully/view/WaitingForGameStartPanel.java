package distribully.view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;
import distribully.model.Player;

public class WaitingForGameStartPanel extends DistribullyPanel implements IObserver {

	private static final long serialVersionUID = -4781135704384241776L;
	private DistribullyModel model;
	private Dimension size;

	public WaitingForGameStartPanel(DistribullyModel model, Dimension size) {
		this.model = model;
		this.size = size;
		
		this.model.addObserver(this);
		
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		render();
	}
	
	public void render() {
		this.removeAll();
		
		DistribullyTextLabel header = new DistribullyTextLabel("Waiting For Everyone to Choose Rules...");
		this.add(header);
		
		for (Player player : model.getGamePlayerList().getPlayers()) {
			this.add(getPlayerPanel(player));
		}
	}
	
	public DistribullyPanel getPlayerPanel(Player player) {
		DistribullyPanel playerPanel = new DistribullyPanel();
		playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		return playerPanel;
	}

	@Override
	public void update(IObservable observable, Object changedObject) {
		this.render();
	}
}
