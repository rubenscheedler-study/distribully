package distribully.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import distribully.controller.ClientListUpdateHandler;
import distribully.controller.CloseWindowHandler;
import distribully.controller.GameState;
import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;

public class DistribullyWindow extends JFrame implements IObserver {

	private static final long serialVersionUID = -6180030798589552918L;
	Logger logger = Logger.getLogger("DistribullyWindow");
	
	//model
	private DistribullyModel model;
	
	//view components
	private JPanel mainPanel;
	private Font font;
	private PlayerOverviewPanel playerOverviewPanel;
	private SelectRulesPanel selectRulesPanel;
	private WaitingForGameStartPanel waitingForGameStartPanel;
	private HandPanel gamePanel;
	private JScrollPane scrollPane;
	
	
	public DistribullyWindow(DistribullyModel model) {
		this.model = model;
		
		//get the size of the monitor
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.height -= 100;
		//window properties
		this.setSize(screenSize.width, screenSize.height);
		this.setVisible(true);
		this.setTitle("Distribully v0.1");
		model.addObserver(this);
		//init of menu
		this.setJMenuBar(new DistribullyMenu(this));
		new ClientListUpdateHandler(this.model);
		
		playerOverviewPanel = new PlayerOverviewPanel(model,this.getSize());
		selectRulesPanel = new SelectRulesPanel(this,this.getSize());
		gamePanel = new HandPanel(model,this.getSize());
		waitingForGameStartPanel = new WaitingForGameStartPanel(model,this.getContentPane().getSize());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new CloseWindowHandler(model));
		
		this.determinePanelToShow();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		
		scrollPane = new JScrollPane(mainPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		scrollPane.getViewport().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				System.out.println("scroll event");
				mainPanel.revalidate();
				mainPanel.repaint();
			}
			
			
		});
		this.add(scrollPane);
		
		this.revalidate();
		this.repaint();
	}
	
	public void setMainPanel(JPanel content) {
		System.out.println(this.getComponentCount());
		this.remove(scrollPane);
		this.mainPanel = content;
		this.add(mainPanel);
		System.out.println("updated main panel");
		this.revalidate();
		this.repaint();
	}
	
	public DistribullyModel getModel() {
		return this.model;
	}

	
	
	@Override
	public void update(IObservable observable, Object gameState) {
		if (gameState instanceof GameState) {
			this.remove(scrollPane);
			determinePanelToShow();	
			scrollPane = new JScrollPane(mainPanel);
			this.add(scrollPane);
			System.out.println("repaint of whole window");
			this.revalidate();
			this.repaint();
		} 
	}
	

	
	public void determinePanelToShow() {
		switch (model.getGAME_STATE()) {
		case SETTING_RULES:
			mainPanel = selectRulesPanel;
			break;
		case WAITING_FOR_GAMESTART:
			mainPanel = waitingForGameStartPanel;
			break;
		case IN_GAME:
			mainPanel = gamePanel;
			break;
		case NOT_PLAYING:
		case INVITING_USERS:
		case IN_LOBBY:
		default:
			mainPanel = playerOverviewPanel;//playerOverviewPanel
			break;
		}
	}
	
	
}
