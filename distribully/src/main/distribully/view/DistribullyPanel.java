package distribully.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

public class DistribullyPanel extends JPanel {

	private static final long serialVersionUID = -1020091315807526967L;
	protected Font headerFont = new Font("Trebuchet MS", Font.BOLD, 20);
	protected Font normalFont = new Font("Trebuchet MS", Font.PLAIN, 12);
	public DistribullyPanel() {
		this.setForeground(new Color(230,230,230));
		this.setBackground(new Color(150,0,0));
		this.setFont(normalFont);
	}
	
	public void setHeaderFont() {
		this.setFont(this.headerFont);
	}
}
