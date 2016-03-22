package distribully.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.model.Card;
import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class BroadcastDrawHandler { //handle card drawing
	public BroadcastDrawHandler(DistribullyModel model, int drawAmount, TurnState nextTurn){
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		model.setReadyToWin(false);

		//Draw the cards, add them to the hand
		for (int i = 0; i < drawAmount; i++) {
			model.getHand().add(Card.getARandomCard());
		}

		//Notify others about what this player has done
		JsonObject message = new JsonObject();
		message.add("turnState",  parser.parse((gson.toJson(nextTurn))).getAsJsonObject());

		new ProducerHandler(message.toString(), "NextTurn" ,model.getMe());
	}

}
