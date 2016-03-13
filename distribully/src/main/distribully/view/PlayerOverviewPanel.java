package distribully.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethod;

import distribully.model.ClientList;
import distribully.model.Player;

public class PlayerOverviewPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -2882716648466999779L;
	//private ArrayList<Player> playerRows = new ArrayList<Player>();
	private ClientList clientList;
	
	public PlayerOverviewPanel(ClientList cl) {
		this.clientList = cl;
		cl.addObserver(this);
		this.render();
	}
	

	
	protected void render() {
		this.removeAll();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMinimumSize(new Dimension(800, clientList.getPlayers().size()*40+80));
		this.setPreferredSize(new Dimension(800, clientList.getPlayers().size()*40+80));
		this.setMaximumSize(new Dimension(800, clientList.getPlayers().size()*40+80));
		//playerRows.forEach(row -> this.add(row));

		this.revalidate();
		this.repaint();
		
	}



	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
