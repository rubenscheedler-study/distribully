package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class LeaveGameHandler implements ActionListener  {

	private DistribullyModel model;
	public LeaveGameHandler(DistribullyModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int leaveGame = JOptionPane.showConfirmDialog (null, 
				"You are in the middle of a game, are you sure you want to stop?", 
				"Confirm",JOptionPane.YES_NO_OPTION); 
		if(leaveGame == JOptionPane.YES_OPTION){
			JsonParser parser = new JsonParser();
			Gson gson = new Gson();

			JsonObject message = new JsonObject();
			message.addProperty("playerName", model.getNickname());
			//If getNextPlayer is null, game hasn't started, so return any player as it will be overwritten anyway. Most convenient is our own name.
			TurnState newState; //Select someone to takeOver your turn, if it is your turn. Else tell everyone the player doesn't change because you leave
			if(model.getTurnState() == null){
				newState = new TurnState(model.getNickname(), 0, 1, model.getNickname() + " has left the game.", false, "");
			}else{
				newState = new TurnState((model.isMyTurn())? model.getNextPlayer() : model.getTurnState().getNextPlayer(), 0, model.getTurnState().getDirection(), model.getNickname() + " has left the game.", false, "");
			}
			JsonObject turnState = parser.parse(gson.toJson(newState)).getAsJsonObject();
			message.add("turnState", turnState);
			new ProducerHandler(message.toString(), "Leave", model.getMe()); //Tell everyone you left
			new BackToMainPageHandler(model); //Return to the main page
		}
	}
}
