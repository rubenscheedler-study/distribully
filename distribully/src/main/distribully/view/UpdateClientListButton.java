package distribully.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import distribully.controller.ClientListUpdateHandler;
import distribully.model.DistribullyModel;

public class UpdateClientListButton extends JMenuItem {

	private static final long serialVersionUID = -5658507871936425835L;

	public UpdateClientListButton(DistribullyModel model) {		
		this.setText("Refresh Available Player List");
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ClientListUpdateHandler(model);
			}
		});
	}
}
