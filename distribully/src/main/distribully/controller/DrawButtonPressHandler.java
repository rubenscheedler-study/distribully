package distribully.controller;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class DrawButtonPressHandler {
	public DrawButtonPressHandler(DistribullyModel model){
		TurnState toSend = model.getTurnState();
		int toDraw = model.getTurnState().getToPick()+1;
		toSend.setNextPlayer(model.getNextPlayer());
		toSend.setAction(model.getNickname() + " has drawn "+ toDraw+" card(s).");
		toSend.setToPick(0);
		new BroadcastDrawHandler(model, toDraw, toSend);
	}
}
