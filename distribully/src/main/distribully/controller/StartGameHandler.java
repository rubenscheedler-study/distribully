package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import distribully.model.DistribullyModel;

public class StartGameHandler implements ActionListener  {

	private DistribullyModel model;
	public StartGameHandler(DistribullyModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		DistribullyController.InviteThreadList.forEach(thread -> thread.closeServer());
		DistribullyController.updateGameHostThread.setIsSettingUpGame(false);
		new ProducerHandler("", "Start", model.getMe());
	}
}
