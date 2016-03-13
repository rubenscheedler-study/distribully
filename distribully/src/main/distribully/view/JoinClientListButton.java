package distribully.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class JoinClientListButton extends JMenuItem {

	private static final long serialVersionUID = 648404151872534455L;

	protected DistribullyWindow container;
	

	public void setContainer(DistribullyWindow c) {
		this.container = c;
	}

	public JoinClientListButton(DistribullyWindow container) {
		this.container = container;
		this.setText("Join Client List");
		
		this.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent actionEvent) {
		    	System.out.println("mand");
		      container.setMainPanel(new PlayerOverviewPanel(container.getModel()));
		    }
		});
	}
}
