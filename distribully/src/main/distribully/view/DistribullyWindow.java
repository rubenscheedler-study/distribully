package distribully.view;

import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import distribully.model.DistribullyModel;

public class DistribullyWindow extends JFrame {

	private static final long serialVersionUID = -6180030798589552918L;
	Logger logger = Logger.getLogger("DistribullyWindow");
	
	//model
	DistribullyModel model;
	
	//view components
	JPanel mainPanel;
	
	public DistribullyWindow(DistribullyModel model) {
		this.model = model;
		
		//window properties
		this.setSize(900, 600);
		this.setVisible(true);
		this.setTitle("Distribully v0.1");
		
		//init of menu
		this.setJMenuBar(new DistribullyMenu(this));
		
		mainPanel = new JPanel();
		this.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		
		this.revalidate();
		this.repaint();
	}
	
	public void setMainPanel(JPanel content) {
		this.mainPanel = content;
		this.revalidate();
		this.repaint();
	}
	
	public DistribullyModel getModel() {
		return this.model;
	}
}
