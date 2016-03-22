package distribully.controller;

import com.google.gson.JsonObject;

import distribully.model.Card;
import distribully.model.DistribullyModel;
import distribully.model.Player;

public class PlayCardHandler { //Broadcast which card you played
	public PlayCardHandler(Card card, Card stackCard, DistribullyModel model){
		//Remove card from hand
		model.getHand().remove(card);

		//Check if hand is empty, then the player is ready to win
		if (model.getHand().isEmpty()) {
			model.setReadyToWin(true);
		}
		
		JsonObject message = new JsonObject();
		message.addProperty("cardId",  card.getNumber());
		message.addProperty("suitId", card.getSuit().getV());
		String ownerName = "";
		for (Player owner : model.getTopOfStacks().keySet()) {
			if (model.getTopOfStacks().get(owner).equals(stackCard)) {
				ownerName = owner.getName();
			}
		}
		
		message.addProperty("stackOwner", ownerName);

		new ProducerHandler(message.toString(), "PlayCard" ,model.getMe());
	}

}
