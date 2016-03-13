package distribully.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

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
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMinimumSize(new Dimension(800, model.getClientList().getPlayers().size()*40+80));
		this.setPreferredSize(new Dimension(800, model.getClientList().getPlayers().size()*40+80));
		this.setMaximumSize(new Dimension(800, model.getClientList().getPlayers().size()*40+80));
		//playerRows.forEach(row -> this.add(row));

		this.revalidate();
		this.repaint();
		
	}

	protected void renderPlayer(Player player) {
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("player overview: received update of client list");
		this.render();
	}
}
