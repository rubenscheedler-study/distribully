//<<<<<<< HEAD:distribully/src/main/distribully/view/DistribullyWindow.java
//package distribully.view;
//
//import java.util.logging.Logger;
//
//import javax.swing.BoxLayout;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import distribully.model.Console;
//
//public class DistribullyWindow extends JFrame {
//
//	private static final long serialVersionUID = -6180030798589552918L;
//	Logger logger = Logger.getLogger("DistribullyWindow");
//	JPanel mainPanel;
//	JPanel consolePanel;
//	
//	public DistribullyWindow() {
//		//window properties
//		this.setSize(800, 600);
//		this.setVisible(true);
//		this.setTitle("Distribully v0.1");
//		
//		//init of menu
//		this.setJMenuBar(new DistribullyMenu());
//		
//		mainPanel = new JPanel();
//		consolePanel = new ConsolePanel(new Console());
//		this.add(mainPanel);
//		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
//		mainPanel.add(consolePanel);
//		
//		
//		this.revalidate();
//		this.repaint();
//	}
//}
//=======
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
	JPanel consolePanel;
	
	public DistribullyWindow() {
		//window properties
		this.setSize(900, 600);
		this.setVisible(true);
		this.setTitle("Distribully v0.1");
		
		//init of menu
		this.setJMenuBar(new DistribullyMenu());
		
		mainPanel = new JPanel();
		consolePanel = new ConsolePanel(new Console());
		this.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		mainPanel.add(consolePanel);
		mainPanel.add(new PlayerOverviewPanel());
		
		this.revalidate();
		this.repaint();
	}
}
//>>>>>>> 2d426a26da268a1bc4a3ccfe4382631147e7e361:distribully/src/distribully/view/DistribullyWindow.java
