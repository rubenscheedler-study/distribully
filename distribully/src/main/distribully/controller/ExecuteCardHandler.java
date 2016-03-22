package distribully.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;
import distribully.model.rules.Rule;

public class ExecuteCardHandler { //Handle a card
	public ExecuteCardHandler(DistribullyModel model, int cardId){
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		
		int direction = 1;
		int toPick = 0;
		TurnState turnState;
		if(model.getChosenRules().containsKey(cardId)){ //Special rule
			Rule rule = model.getChosenRules().get(cardId);
			turnState = rule.execute();
		}else{
			direction = model.getTurnState().getDirection();
			toPick = model.getTurnState().getToPick();
			turnState = new TurnState(model.getNextPlayer(),toPick,direction, "", false,model.getNickname());
		}
		if(turnState.getToPick() == model.getTurnState().getToPick() && turnState.getToPick() > 0){ //Number of card to pick did not increase, tell user to draw them
			turnState.setToPick(0);
			JsonObject message = new JsonObject();
			message.addProperty("drawAmount",  model.getTurnState().getToPick());
			message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

			new ProducerHandler(message.toString(),"MustDraw" ,model.getMe());
		}
		else{
			JsonObject message = new JsonObject();
			message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

			new ProducerHandler(message.toString(), "NextTurn" ,model.getMe());
		}
	}
}
