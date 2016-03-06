package distribully.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PlayerOverviewPanel extends JPanel {

	private static final long serialVersionUID = -2882716648466999779L;
	private ArrayList<PlayerAddRowPanel> playerRows = new ArrayList<PlayerAddRowPanel>();
	private JButton addPlayerButton = new JButton("add player");
	private JButton removePlayerButton = new JButton("remove player");
	
	public PlayerOverviewPanel() {
		
		//at least enter info of 1 other player
		playerRows.add(new PlayerAddRowPanel());
		
		
		addPlayerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPlayerRow();
			}
		});
		
		removePlayerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removePlayerRow();
			}
		});
		
		this.render();
	}
	
	
	protected void addPlayerRow() {
		playerRows.add(new PlayerAddRowPanel());
		render();
	}
	
	protected void removePlayerRow() {
		playerRows.remove(playerRows.size()-1);
		render();
	}
	
	protected void render() {
		this.removeAll();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setMinimumSize(new Dimension(800, playerRows.size()*40+80));
		this.setPreferredSize(new Dimension(800, playerRows.size()*40+80));
		this.setMaximumSize(new Dimension(800, playerRows.size()*40+80));
		playerRows.forEach(row -> this.add(row));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setMinimumSize(new Dimension(800,40));
		buttonPanel.setPreferredSize(new Dimension(800,40));
		buttonPanel.setMaximumSize(new Dimension(800,40));
		buttonPanel.add(addPlayerButton);
		buttonPanel.add(removePlayerButton);
		
		this.add(buttonPanel);
		this.revalidate();
		this.repaint();
		
	}
}
