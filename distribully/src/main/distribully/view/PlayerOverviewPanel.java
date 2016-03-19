package distribully.view;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;


import javax.swing.BoxLayout;

import javax.swing.JLabel;

import distribully.controller.GameState;

import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;
import distribully.model.Player;

public class PlayerOverviewPanel extends DistribullyPanel implements IObserver {

	private static final long serialVersionUID = -2882716648466999779L;
	//private ArrayList<Player> playerRows = new ArrayList<Player>();
	private DistribullyModel model;
	private Dimension size;
	
	public PlayerOverviewPanel(DistribullyModel model, Dimension size) {
		System.out.println("created player overview panel:" + size.getWidth() + "," + size.getHeight());
		this.model = model;
		this.size = size;
		model.getOnlinePlayerList().addObserver(this);
		model.getGamePlayerList().addObserver(this);
		model.addObserver(this);
		this.render();
	}
	
	protected void render() {
		this.removeAll();
		
		//determine which list of players to render: game members or all online
		ArrayList<Player> players;
		if (model.getGAME_STATE() == GameState.IN_LOBBY) {
			players = model.getGamePlayerList().getPlayers();
		} else {
			players = model.getOnlinePlayerList().getPlayers();
		}
		
		
		
		//System.out.println("render::playerCount:" + players.size());
		//remove self from the list of available players
		//players.removeIf(player -> player.getName().equals(model.getNickname()));
				
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		
		
		if (players.size() == 0) {
			this.add(new JLabel("No available players"));
		} else {
			
			this.add(getHeaderPanel());
			
			for (Player p : players) {
				//do not render self
				if (p.getName().equals(model.getNickname())) {
					continue;
				}
				this.renderPlayer(p);
			}


		}
		this.revalidate();
		this.repaint();
		
	}

	
	protected DistribullyPanel getHeaderPanel() {
		DistribullyPanel headerPanel = new DistribullyPanel();
		headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setMinimumSize(new Dimension(this.size.width, 40));
		headerPanel.setPreferredSize(new Dimension(this.size.width, 40));
		headerPanel.setMaximumSize(new Dimension(this.size.width, 40));
		if (model.getGAME_STATE() == GameState.INVITING_USERS) {
			//add a button to actually start the game
			headerPanel.add(new StartGameButton(model));
		} else if (model.getGAME_STATE() == GameState.IN_LOBBY) {
			//add a button to leave the lobby, that is: remove yourself from the game player list
			headerPanel.add(new LeaveLobbyButton(model));
		}
		
		return headerPanel;
	}
	
	
	protected void renderPlayer(Player player) {
		DistribullyPanel playerPanel = new DistribullyPanel();
		playerPanel.setMinimumSize(new Dimension(this.size.width, 40));
		playerPanel.setPreferredSize(new Dimension(this.size.width, 40));
		playerPanel.setMaximumSize(new Dimension(this.size.width, 40));
		
		playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		DistribullyTextLabel nameLabel = new DistribullyTextLabel(player.getName());
		nameLabel.setPreferredSize(new Dimension(400,40));
		playerPanel.add(nameLabel);
		
		if (model.getGAME_STATE() == GameState.INVITING_USERS) {
			playerPanel.add(getInvitationPanel(player));
		} else if (model.getGAME_STATE() == GameState.IN_LOBBY) {
			playerPanel.add(getLobbyPanel(player));
		}
		
		this.add(playerPanel);
	}

	/**
	 * returns a panel containing either a working invite button, or an invitation state
	 * @param player Player to render to a panel
	 * @return the panel
	 */
	private DistribullyPanel getInvitationPanel(Player player) {
		DistribullyPanel playerPanel = new DistribullyPanel();
		if (player.isAvailable()) {
			InviteButton inviteButton = null;
			//check if player was already invited
			if (model.getInviteStates().containsKey(player.getName())) {
				playerPanel.add(new JLabel(model.getInviteStates().get(player.getName())));
			} else {
				inviteButton = new InviteButton(model, player.getName());
				playerPanel.add(inviteButton);
			}
			
		} else {
			DistribullyTextLabel unavailableLabel = new DistribullyTextLabel("unavailable");
			playerPanel.add(unavailableLabel);
		}
		return playerPanel;
	}
	
	
	private DistribullyPanel getLobbyPanel(Player player) {
		DistribullyPanel playerPanel = new DistribullyPanel();
		
		return playerPanel;
	}
	
	@Override
	public void update(IObservable observable) {
		//System.out.println("player overview: received update of client list");
		this.render();
	}
}
