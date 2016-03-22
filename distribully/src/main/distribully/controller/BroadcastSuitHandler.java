package distribully.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class BroadcastSuitHandler {//Tell everyone which suit you picked
	public BroadcastSuitHandler(DistribullyModel model, int suiteId){
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		TurnState turnState = new TurnState(model.getNextPlayer(),model.getTurnState().getToPick(),model.getTurnState().getDirection(),model.getNickname() + " changed the suit of the stack of " + model.getTurnState().getLastStack() + ".", false, model.getTurnState().getLastStack());

		JsonObject message = new JsonObject();

		message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());
		message.addProperty("cardSuit", suiteId);

		new ProducerHandler(message.toString(), "ChooseSuit",model.getMe());
	}
}
