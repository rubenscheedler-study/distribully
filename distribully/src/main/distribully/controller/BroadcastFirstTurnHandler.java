package distribully.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class BroadcastFirstTurnHandler { //Tell everyone who gets the first turn
	public BroadcastFirstTurnHandler(DistribullyModel model){
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		int i = (int)(Math.random()*(double)model.getGamePlayerList().getPlayers().size());
		String nextPlayer = model.getGamePlayerList().getPlayers().get(i).getName();
		int direction = 1;
		int toPick = 0;
		TurnState turnState = new TurnState(nextPlayer,toPick,direction, "", false, "");

		JsonObject message = new JsonObject();
		message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

		new ProducerHandler(message.toString(),"NextTurn" ,model.getMe());
	}
}
