package distribully.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;

public class StartGameButton extends JMenuItem{

	private static final long serialVersionUID = -3732106117892923335L;

	public StartGameButton() {
		this.setText("start game");

		this.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent actionEvent) {
		      System.out.println("click");
		    }
		});
	}

}
