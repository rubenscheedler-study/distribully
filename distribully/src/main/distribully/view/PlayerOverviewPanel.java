package distribully.view;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import distribully.controller.GameState;
import distribully.controller.InviteUserHandler;
import distribully.model.DistribullyModel;
import distribully.model.Player;

public class PlayerOverviewPanel extends DistribullyPanel implements Observer {

	private static final long serialVersionUID = -2882716648466999779L;
	//private ArrayList<Player> playerRows = new ArrayList<Player>();
	private DistribullyModel model;
	private Dimension size;
	
	public PlayerOverviewPanel(DistribullyModel model, Dimension size) {
		System.out.println("created player overview panel:" + size.getWidth() + "," + size.getHeight());
		this.model = model;
		this.size = size;
		model.getClientList().addObserver(this);
		this.render();
	}
	
	protected void render() {
		this.removeAll();
		ArrayList<Player> players = model.getClientList().getPlayers();
		
		//remove self from the list of available players
		//players.removeIf(player -> player.getName().equals(model.getNickname()));
				
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		if (players.size() == 0) {
			this.add(new JLabel("No available players"));
		} else {
			players.forEach(player -> this.renderPlayer(player));
		}
		this.revalidate();
		this.repaint();
		
	}

	protected void renderPlayer(Player player) {
		JPanel playerPanel = new DistribullyPanel();
		playerPanel.setMinimumSize(new Dimension(this.size.width, 40));
		playerPanel.setPreferredSize(new Dimension(this.size.width, 40));
		playerPanel.setMaximumSize(new Dimension(this.size.width, 40));
		
		playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		DistribullyTextLabel nameLabel = new DistribullyTextLabel(player.getName());
		nameLabel.setPreferredSize(new Dimension(400,40));
		playerPanel.add(nameLabel);
		if (model.getGAME_STATE() == GameState.INVITING_USERS) {
			//define an invite button with behavior
			if (player.isAvailable()) {
				InviteButton inviteButton = new InviteButton(model, player.getName());
				
				playerPanel.add(inviteButton);
			} else {
				DistribullyTextLabel unavailableLabel = new DistribullyTextLabel("unavailable");
				playerPanel.add(unavailableLabel);
			}
			
		}
		this.add(playerPanel);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("player overview: received update of client list");
		this.render();
	}
}
