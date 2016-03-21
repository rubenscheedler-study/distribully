package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.DistribullyModel;

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
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(model.getMe().getIp());
			Connection connection;
			try {
				connection = factory.newConnection();

				Channel channel = connection.createChannel();

				channel.exchangeDeclare(model.getNickname(), "fanout");

				JsonObject message = new JsonObject();
				message.addProperty("playerName", model.getNickname());

				channel.basicPublish(model.getNickname(), "Leave", null, message.toString().getBytes());
				System.out.println(" [x] Sent '" + message + "'");

				channel.close();
				connection.close();
			} catch (IOException | TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			DistribullyController.consumerThread.setPlaying(false);
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
