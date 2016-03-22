package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class LeaveGameHandler implements ActionListener  {

	DistribullyModel model;
	public LeaveGameHandler(DistribullyModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int leaveGame = JOptionPane.showConfirmDialog (null, 
				"You are in the middle of a game, are you sure you want to stop?", 
				"Confirm",JOptionPane.YES_NO_OPTION); 
		if(leaveGame == JOptionPane.YES_OPTION){
			DistribullyController.consumerThread.setPlaying(false);
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(model.getMe().getIp());
			Connection connection;
			try {
				connection = factory.newConnection();

				Channel channel = connection.createChannel();

				channel.exchangeDeclare(model.getNickname(), "fanout");
				JsonParser parser = new JsonParser();
				Gson gson = new Gson();

				JsonObject message = new JsonObject();
				message.addProperty("playerName", model.getNickname());
				//If getNxtPlayer is null, game hasn't started, so return any player as it will be overwritten anyway. Most convenient is our own name.
				TurnState newState = new TurnState(model.getNextPlayer(), 0, (model.getTurnState() == null)? 1 : model.getTurnState().getDirection(), model.getNickname() + " has left the game.", false, "");
				JsonObject turnState = parser.parse(gson.toJson(newState)).getAsJsonObject();
				message.add("turnState", turnState);

				channel.basicPublish(model.getNickname(), "Leave", null, message.toString().getBytes());
				System.out.println(" [x] Sent '" + message + "'");

				channel.close();
				connection.close();
			} catch (IOException | TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//set available for invites
			model.getMe().setAvailable(true);
			//change game state
			DistribullyController.waitForInviteThread = new WaitForInviteThread(model);
			//set game state
			model.setGAME_STATE(GameState.NOT_PLAYING);
			//refresh the online player list
			new ClientListUpdateHandler(model);
		}
	}
}
