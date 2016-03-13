package distribully.view;

import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import distribully.model.Console;

public class DistribullyWindow extends JFrame {

	private static final long serialVersionUID = -6180030798589552918L;
	Logger logger = Logger.getLogger("DistribullyWindow");
	JPanel mainPanel;
	
	public DistribullyWindow() {
		//window properties
		this.setSize(900, 600);
		this.setVisible(true);
		this.setTitle("Distribully v0.1");
		
		//init of menu
		this.setJMenuBar(new DistribullyMenu(this));
		
		mainPanel = new JPanel();
		this.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		mainPanel.add(new PlayerOverviewPanel());
		
		this.revalidate();
		this.repaint();
	}
	
	public void setMainPanel(JPanel content) {
		this.mainPanel = content;
		this.revalidate();
		this.repaint();
	}
}
