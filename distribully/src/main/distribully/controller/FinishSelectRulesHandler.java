package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import com.google.gson.JsonObject;

import distribully.model.DistribullyModel;

public class FinishSelectRulesHandler  implements ActionListener {

	private DistribullyModel model;
	public FinishSelectRulesHandler(DistribullyModel model) {
		this.model = model;	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(model.getChosenRules().size() == model.getAllRules().size()){ //Are all rules assigned?
			JsonObject message = new JsonObject();
			message.addProperty("playerName", model.getNickname());
			
			if(model.getGAME_STATE() != GameState.IN_GAME){ //Since the queue may update the state before this does, this is needed.
				model.setGAME_STATE(GameState.WAITING_FOR_GAMESTART);
			}
			new ProducerHandler(message.toString(), "Rules", model.getMe());
		}else{
			JOptionPane.showMessageDialog(null,
					"Every rule must be assigned.",
					"Missing rule",
					JOptionPane.ERROR_MESSAGE);
		}

	}

}
