package distribully.view;

import java.awt.Color;

import javax.swing.JLabel;

public class DistribullyTextLabel extends JLabel {

	private static final long serialVersionUID = 7576195558106444757L;

	public DistribullyTextLabel() {
		this.setForeground(new Color(230,230,230));
	}
	
	public DistribullyTextLabel(String labelText) {
		super(labelText);
		this.setForeground(new Color(230,230,230));
	}
}
