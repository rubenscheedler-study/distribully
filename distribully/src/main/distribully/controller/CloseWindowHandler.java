package distribully.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class CloseWindowHandler extends WindowAdapter {

	DistribullyModel model;

	private static Logger logger;
	public CloseWindowHandler(DistribullyModel model){
		this.model = model;
		logger = LoggerFactory.getLogger("controller.CloseWindowHandler");
	}
	@Override
	public void windowClosing(WindowEvent e)
	{ 
		if (model.getGAME_STATE() == GameState.IN_GAME || model.getGAME_STATE() == GameState.SETTING_RULES){
			int leaveGame = JOptionPane.showConfirmDialog (null, 
					"You are in the middle of a game, are you sure you want to quit?", 
					"Confirm",JOptionPane.YES_NO_OPTION); 
			if(leaveGame == JOptionPane.YES_OPTION){
				ConnectionFactory factory = new ConnectionFactory();
				factory.setHost(model.getMe().getIp());
				Connection connection;
				try {
					connection = factory.newConnection();

					Channel channel = connection.createChannel();

					channel.exchangeDeclare(model.getNickname(), "fanout");

					JsonObject message = new JsonObject();
					message.addProperty("playerName", model.getNickname());
					Gson gson = new Gson();
					JsonParser parser = new JsonParser();
					//If getNxtPlayer is null, game hasn't started, so return any player as it will be overwritten anyway. Most convenient is our own name.
					TurnState newState = new TurnState(model.getNextPlayer(), 0, (model.getTurnState() == null)? 1 : model.getTurnState().getDirection(), model.getNickname() + " has left the game.", false, "");
					JsonObject turnState = parser.parse(gson.toJson(newState)).getAsJsonObject();
					message.add("turnState", turnState);

					channel.basicPublish(model.getNickname(), "Leave", null, message.toString().getBytes());
					logger.info(" [x] Sent '" + message + "'");
					channel.close();
					connection.close();
				} catch (IOException | TimeoutException e1) {
					//Doesn't matter what happens here, since we are closing the application anyway. A log for debugging will suffice.
					logger.error("Error while closing application");
					
				}
			}else{
				return;
			}
		}
		model.getMe().deleteFromServer();
		if(model.getGAME_STATE() == GameState.IN_LOBBY){
			model.getGamePlayerList().deleteFromGame(model.getNickname(),model.getCurrentHostName());
		} else if (model.getGAME_STATE() == GameState.INVITING_USERS) {
			model.getGamePlayerList().deleteGameList(model.getNickname());//=current host name
		}
		logger.info("Closed game");
		System.exit(0);

	}
}
