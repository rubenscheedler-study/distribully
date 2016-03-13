package distribully.view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NameAskWindow extends JFrame {

	private static final long serialVersionUID = -1482360288991363097L;

	public NameAskWindow() {
		this.setVisible(true);
		this.setSize(200, 100);
		this.setLocation(400, 400);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		JPanel namePanel = new JPanel();
		namePanel.setMinimumSize(new Dimension(200,40));
		namePanel.setPreferredSize(new Dimension(200,40));
		namePanel.setMaximumSize(new Dimension(200,40));
		namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel nameLabel = new JLabel("nickname:");
		namePanel.add(nameLabel);
		
		JTextField nameTextField = new JTextField();
		nameTextField.setMinimumSize(new Dimension(100,40));
		nameTextField.setPreferredSize(new Dimension(100,40));
		nameTextField.setMaximumSize(new Dimension(100,40));
		namePanel.add(nameTextField);		
		this.add(namePanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton chooseButton = new JButton("submit");
		buttonPanel.add(chooseButton);
		
		this.add(buttonPanel);
	}
}
