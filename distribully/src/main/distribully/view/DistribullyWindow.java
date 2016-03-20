package distribully.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import distribully.controller.ClientListUpdateHandler;
import distribully.controller.CloseWindowHandler;
import distribully.model.DistribullyModel;

public class DistribullyWindow extends JFrame {

	private static final long serialVersionUID = -6180030798589552918L;
	Logger logger = Logger.getLogger("DistribullyWindow");
	
	//model
	private DistribullyModel model;
	
	//view components
	private JPanel mainPanel;
	private Font font;
	
	public DistribullyWindow(DistribullyModel model) {
		this.model = model;
		
		//get the size of the monitor
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.height -= 100;
		//window properties
		this.setSize(screenSize.width, screenSize.height);
		this.setVisible(true);
		this.setTitle("Distribully v0.1");
		
		//init of menu
		this.setJMenuBar(new DistribullyMenu(this));
		new ClientListUpdateHandler(this.model);
		//mainPanel = new PlayerOverviewPanel(this.model, this.getSize());
		//mainPanel = new SelectRulesPanel(this,this.getSize());
		mainPanel = new GamePanel(this.getModel(),this.getSize());
		this.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addWindowListener(new CloseWindowHandler(model));
		
		this.revalidate();
		this.repaint();
	}
	
	public void setMainPanel(JPanel content) {
		System.out.println(this.getComponentCount());
		this.remove(mainPanel);
		this.mainPanel = content;
		this.add(mainPanel);
		System.out.println("updated main panel");
		this.revalidate();
		this.repaint();
	}
	
	public DistribullyModel getModel() {
		return this.model;
	}
	
	

}
