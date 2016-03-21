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
		this.model.getGamePlayerList().addObserver(this);
		
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
		playerPanel.setMinimumSize(new Dimension(this.size.width,40));
		playerPanel.setPreferredSize(new Dimension(this.size.width,40));
		playerPanel.setMaximumSize(new Dimension(this.size.width,40));
		
		DistribullyTextLabel name = new DistribullyTextLabel(player.getName());
		name.setPreferredSize(new Dimension(200,40));
		DistribullyTextLabel choosenrules = new DistribullyTextLabel(player.isReadyToPlay() ? "ready" : "still choosing rules");
		choosenrules.setPreferredSize(new Dimension(200,40));
		
		playerPanel.add(name);
		playerPanel.add(choosenrules);
		
		return playerPanel;
	}

	@Override
	public void update(IObservable observable, Object changedObject) {
		this.render();
	}
}
