
package distribully.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class StartGameButton extends JMenuItem{

	private static final long serialVersionUID = -3732106117892923335L;

	DistribullyWindow container;
	
	public StartGameButton(DistribullyWindow container) {
		this.container = container;
		
		this.setText("start game");
		
		this.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent actionEvent) {
		      container.setMainPanel(new JPanel());
		    }
		});
		
	}

}

