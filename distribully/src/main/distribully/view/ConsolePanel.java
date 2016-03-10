//<<<<<<< HEAD:distribully/src/main/distribully/view/ConsolePanel.java
//package distribully.view;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.util.ArrayList;
//import java.util.Observable;
//import java.util.Observer;
//
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//
//import distribully.model.Console;
//
//public class ConsolePanel extends JPanel implements Observer {
//
//	private static final long serialVersionUID = 1L;
//	private Console console;
//	
//	public ConsolePanel(Console c) {
//		this.console = c;
//		c.addObserver(this);
//		
//		this.setMinimumSize(new Dimension(800, 400));
//		this.setPreferredSize(new Dimension(800, 400));
//		this.setMaximumSize(new Dimension(800, 400));
//		this.setBackground(new Color(100,0,0));
//	}
//	
//	protected void paintComponent(Graphics g) {
//		g.setColor(new Color(255,255,255));
//		super.paintComponent(g);
//		ArrayList<String> messages = console.getMessages();
//		for (int currentY = 0; currentY < messages.size(); currentY++) {
//			g.drawString(messages.get(currentY),0,currentY*10+10);
//		}
//
//	}
//
//	@Override
//	public void update(Observable console, Object dunno) {
//		this.repaint();
//	}
//}
//=======
package distribully.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import distribully.model.Console;

public class ConsolePanel extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private Console console;
	
	public ConsolePanel(Console c) {
		this.console = c;
		c.addObserver(this);
		
		this.setMinimumSize(new Dimension(800, 100));
		this.setPreferredSize(new Dimension(800, 100));
		this.setMaximumSize(new Dimension(800, 100));
		this.setBackground(new Color(100,0,0));
	}
	
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(255,255,255));
		super.paintComponent(g);
		ArrayList<String> messages = console.getMessages();
		for (int currentY = 0; currentY < messages.size(); currentY++) {
			g.drawString(messages.get(currentY),0,currentY*10+10);
		}

	}

	@Override
	public void update(Observable console, Object dunno) {
		this.repaint();
	}
}
//>>>>>>> 2d426a26da268a1bc4a3ccfe4382631147e7e361:distribully/src/distribully/view/ConsolePanel.java
