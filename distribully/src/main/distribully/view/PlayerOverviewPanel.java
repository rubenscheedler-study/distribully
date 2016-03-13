package distribully.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
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

public class PlayerOverviewPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -2882716648466999779L;
	//private ArrayList<Player> playerRows = new ArrayList<Player>();
	private DistribullyModel model;
	
	public PlayerOverviewPanel(DistribullyModel model) {
		this.model = model;
		model.getClientList().addObserver(this);
		this.render();
	}
	
	protected void render() {
		this.removeAll();
		ArrayList<Player> players = model.getClientList().getPlayers();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMinimumSize(new Dimension(800, players.size()*40+80));
		this.setPreferredSize(new Dimension(800, players.size()*40+80));
		this.setMaximumSize(new Dimension(800, players.size()*40+80));
		if (players.size() == 0) {
			this.add(new JLabel("No available players"));
		} else {
			players.forEach(player -> this.renderPlayer(player));
		}
		this.revalidate();
		this.repaint();
		
	}

	protected void renderPlayer(Player player) {
		JPanel playerPanel = new JPanel();
		
		playerPanel.setMinimumSize(new Dimension(800, 40));
		playerPanel.setPreferredSize(new Dimension(800, 40));
		playerPanel.setMaximumSize(new Dimension(800, 40));
		
		playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel nameLabel = new JLabel(player.getName());
		playerPanel.add(nameLabel);
		if (model.getGAME_STATE() == GameState.INVITING_USERS) {
			//define an invite button with behavior
			JButton inviteButton = new JButton("Invite");
			inviteButton.addActionListener(new InviteUserHandler(player.getName()));
			
			playerPanel.add(inviteButton);
		}
		this.add(playerPanel);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("player overview: received update of client list");
		this.render();
	}
}
