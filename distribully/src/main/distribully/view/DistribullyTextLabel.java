package distribully.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class DistribullyTextLabel extends JLabel {

	private static final long serialVersionUID = 7576195558106444757L;

	protected Font headerFont = new Font("Trebuchet MS", Font.BOLD, 20);
	protected Font normalFont = new Font("Trebuchet MS", Font.PLAIN, 12);
	
	public DistribullyTextLabel() {
		this.setForeground(new Color(230,230,230));
	}
	
	public DistribullyTextLabel(String labelText) {
		super(labelText);
		this.setForeground(new Color(230,230,230));
	}
	
	public void setHeaderFont() {
		this.setFont(this.headerFont);
		this.revalidate();
		this.repaint();
	}
}
