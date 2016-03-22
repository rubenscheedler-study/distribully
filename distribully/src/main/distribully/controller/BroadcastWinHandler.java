package distribully.controller;

import com.google.gson.JsonObject;

import distribully.model.DistribullyModel;

public class BroadcastWinHandler {
	public BroadcastWinHandler(DistribullyModel model){//Tell everyone you won
		JsonObject message = new JsonObject();
		message.addProperty("playerWinner", model.getNickname());

		new ProducerHandler(message.toString(), "Win",model.getMe());
	}
}
