package distribully.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.ShutdownSignalException;

import distribully.model.DistribullyModel;
import distribully.model.Player;

public class GameConsumerThread extends Thread{

	DistribullyModel model;
	String queueName;
	Channel channel;
	JsonParser parser;
	Gson gson;
	boolean playing;
	public GameConsumerThread(DistribullyModel model){
		this.model = model;
		new ClientListUpdateHandler(model); //Ensure the playerList is up to date
		ConnectionFactory factory = new ConnectionFactory();  
		String hostName = model.getCurrentHostName();
		Player host = model.getOnlinePlayerList().getPlayerByNickname(hostName);
		factory.setHost(host.getIp());
		parser = new JsonParser();
		gson = new Gson();
		playing = true;
		Connection connection = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(hostName, "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, hostName, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.start();

	}

	public void run(){
		while(playing){
			GetResponse response = null;
			try {
				response = channel.basicGet(queueName, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (response == null) { 
				//pblblbllb -> no messages
			}else{
				switch(response.getEnvelope().getRoutingKey()){
				case "Start":
					System.out.println("Game is starting!");
					if(DistribullyController.lobbyThread != null){
						DistribullyController.lobbyThread.setInLobby(false);
					}
					new ClientListUpdateHandler(model);
					model.getGamePlayerList().getPlayers().forEach(player -> initPlayerExchange(player));
					model.setGAME_STATE(GameState.SETTING_RULES);
					
					break;
				case "Leave":
					JsonElement jeLeave = parser.parse(new String(response.getBody()));
					String playerNameLeave = jeLeave.getAsJsonObject().get("playerName").toString();
					model.getGamePlayerList().getPlayers().removeIf(player -> player.getName().equals(playerNameLeave));
					System.out.println(playerNameLeave + " left");
					break;
				case "Rules":
					JsonElement jeRule = parser.parse(new String(response.getBody()));
					String playerNameRule = jeRule.getAsJsonObject().get("playerName").toString();
					System.out.println("Rules from  "+ playerNameRule + " received");
					break;
				case "PlayCard":
					break;
				}
			}

		}
	}

	private void initPlayerExchange(Player player){
		ConnectionFactory factory = new ConnectionFactory();  
		String playerName = player.getName();
		factory.setHost(player.getIp());
		Connection connection = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(playerName, "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, playerName, "");
			System.out.println("host connected: " + playerName);
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
