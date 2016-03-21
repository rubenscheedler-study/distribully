package distribully.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import distribully.model.DistribullyModel;
import distribully.model.Player;

public class GameConsumerThread extends Thread{

	DistribullyModel model;
	String queueName;
	Channel channel;
	boolean playing;
	public GameConsumerThread(DistribullyModel model){
		this.model = model;
		new ClientListUpdateHandler(model); //Ensure the playerList is up to date
		String hostName = model.getCurrentHostName();
		Player host = model.getOnlinePlayerList().getPlayerByNickname(hostName);
		playing = true;
		try {
			initPlayerExchange(host);
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
			queueName = channel.queueDeclare(model.getNickname(), false, false, false, null).getQueue();
			channel.exchangeDeclare(playerName, "fanout");
			channel.queueBind(queueName, playerName, "");
			Consumer consumer = new MessageConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			System.out.println("host connected: " + playerName);
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class MessageConsumer extends DefaultConsumer{
		JsonParser parser;
		public MessageConsumer(Channel channel) {
			super(channel);
			parser = new JsonParser();
		}

		@Override
	    public void handleDelivery(String consumerTag, Envelope envelope,
	                               AMQP.BasicProperties properties, byte[] body) throws IOException {
	  	  switch(envelope.getRoutingKey()){
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
					JsonElement jeLeave = parser.parse(new String(body));
					String playerNameLeave = jeLeave.getAsJsonObject().get("playerName").toString();
					model.getGamePlayerList().getPlayers().removeIf(player -> player.getName().equals(playerNameLeave));
					System.out.println(playerNameLeave + " left");
					break;
				case "Rules":
					JsonElement jeRule = parser.parse(new String(body));
					String playerNameRule = jeRule.getAsJsonObject().get("playerName").toString();
					System.out.println("Rules from  "+ playerNameRule + " received");
					break;
				case "PlayCard":
					break;
				}
	    }
	}
}
