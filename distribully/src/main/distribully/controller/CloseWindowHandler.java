package distribully.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class CloseWindowHandler extends WindowAdapter {

	private DistribullyModel model;
	private static Logger logger;

	public CloseWindowHandler(DistribullyModel model){
		this.model = model;
		logger = LoggerFactory.getLogger("controller.CloseWindowHandler");
	}
	@Override
	public void windowClosing(WindowEvent e)
	{   //Override the window close handling
		if (model.getGAME_STATE() == GameState.IN_GAME || model.getGAME_STATE() == GameState.SETTING_RULES){
			int leaveGame = JOptionPane.showConfirmDialog (null, 
					"You are in the middle of a game, are you sure you want to quit?", 
					"Confirm",JOptionPane.YES_NO_OPTION); 
			if(leaveGame == JOptionPane.YES_OPTION){
				JsonObject message = new JsonObject();
				message.addProperty("playerName", model.getNickname());
				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				//If getNextPlayer is null, game hasn't started, so return any player as it will be overwritten anyway. Most convenient is our own name.
				TurnState newState;
				if(model.getTurnState() == null){
					newState = new TurnState(model.getNickname(), 0, 1, model.getNickname() + " has left the game.", false, "");
				}else{
					newState = new TurnState((model.isMyTurn())? model.getNextPlayer() : model.getTurnState().getNextPlayer(), 0, model.getTurnState().getDirection(), model.getNickname() + " has left the game.", false, "");
				}
				JsonObject turnState = parser.parse(gson.toJson(newState)).getAsJsonObject();
				message.add("turnState", turnState);
				new ProducerHandler(message.toString(), "Leave", model.getMe());
			}else{
				return; //User does not want to close
			}
		}
		model.getMe().deleteFromServer();
		if(model.getGAME_STATE() == GameState.IN_LOBBY){
			model.getGamePlayerList().deleteFromGame(model.getNickname(),model.getCurrentHostName());
		} else if (model.getGAME_STATE() == GameState.INVITING_USERS) {
			model.getGamePlayerList().deleteGameList(model.getNickname());//Since you are the host
		}
		logger.info("Closed game");
		System.exit(0);

	}
}
