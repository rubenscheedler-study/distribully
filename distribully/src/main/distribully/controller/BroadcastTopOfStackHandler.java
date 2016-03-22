package distribully.controller;

import com.google.gson.JsonObject;

import distribully.model.Card;
import distribully.model.DistribullyModel;

public class BroadcastTopOfStackHandler { //Randomly generate a top of stack for yourself
	public BroadcastTopOfStackHandler(DistribullyModel model){
		Card card = Card.getARandomCard();

		JsonObject message = new JsonObject();
		message.addProperty("cardId", card.getNumber());
		message.addProperty("cardSuit", card.getSuit().getV());
		message.addProperty("playerName", model.getNickname());
		new ProducerHandler(message.toString(), "InitStack" ,model.getMe());
	}
}
