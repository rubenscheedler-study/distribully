package distribully.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.Card;
import distribully.model.DistribullyModel;

public class PlayCardHandler {
	
	private DistribullyModel model;
	public PlayCardHandler(DistribullyModel model, Card card) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(model.getMe().getIp());
		Connection connection;
		try {
			connection = factory.newConnection();

			Channel channel = connection.createChannel();

			channel.exchangeDeclare(model.getNickname(), "fanout");

			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject message = new JsonObject();
			message.addProperty("action", "");
			message.add("card",  parser.parse((gson.toJson(card))).getAsJsonObject());

			channel.basicPublish(model.getNickname(), "PlayCard", null, message.toString().getBytes());
			System.out.println(" [x] Sent '" + message + "'");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
