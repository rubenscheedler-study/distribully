package distribully.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import distribully.controller.DistribullyController;
import distribully.model.DistribullyModel;

public class StartGameButton extends JButton {

	private static final long serialVersionUID = -3732106117892923335L;

	private DistribullyModel model;
	
	public StartGameButton(DistribullyModel model) {
		this.setText("Start Game");
		this.model = model;
		
		
		this.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
		    for(int i = 0; i < DistribullyController.InviteThreadList.size(); i++){
		    	DistribullyController.InviteThreadList.get(i).closeServer();
		    }
		    System.out.println(DistribullyController.InviteThreadList.size());
		  }

		});
	}
}
