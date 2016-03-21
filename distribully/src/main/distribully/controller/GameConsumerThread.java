package distribully.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
			//TODO : WAT DAN
		}
	}
	
	public void setPlaying(boolean playing){
		this.playing = playing;
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
					String playerNameLeave = jeLeave.getAsJsonObject().get("playerName").getAsString();
					model.getGamePlayerList().getPlayers().removeIf(player -> player.getName().equals(playerNameLeave));
					System.out.println(playerNameLeave + " left");
					//TODO: view?
					break;
				case "Rules":
					JsonElement jeRule = parser.parse(new String(body));
					String playerNameRule = jeRule.getAsJsonObject().get("playerName").getAsString();
					System.out.println("Rules from  "+ playerNameRule + " received");
					Player player = model.getGamePlayerList().getPlayerByNickname(playerNameRule);
					player.setReadyToPlay(true);
					if(model.getGamePlayerList().getPlayers().stream().allMatch(p->p.isReadyToPlay())){
						model.setGAME_STATE(GameState.IN_GAME);
					}
					break;
				case "PlayCard":
					JsonObject jeCard = parser.parse(new String(body)).getAsJsonObject();
					int cardId = Integer.parseInt(jeCard.get("cardId").getAsString());
					int cardSuite = Integer.parseInt(jeCard.get("cardSuite").getAsString());
					String playerName = jeCard.get("playerName").getAsString();
					System.out.println("Card "+ cardId + " from suite " + cardSuite +" played on stack of "+ playerName); //TODO cardSuite parser
					//TODO: View
					break;
				case "NextTurn":
					JsonObject jeTurn = parser.parse(new String(body)).getAsJsonObject();
					String action = jeTurn.get("action").getAsString();
					String playerNameNext = jeTurn.get("playerName").getAsString();
					System.out.println("Next player is "+ playerNameNext +" by action " + action);
					//TODO: View
					break;
				case "ChooseSuite":
					JsonObject jeSuite = parser.parse(new String(body)).getAsJsonObject();
					int suite = Integer.parseInt(jeSuite.get("cardSuite").getAsString());
					String playerNext = jeSuite.get("playerNextName").getAsString();
					String playerCurrent = jeSuite.get("playerName").getAsString();
					System.out.println("Next player is "+ playerNext +", new suite on "+ playerCurrent +" is " + suite); //suite parse
					//TODO: View
					break;
				case "Win":
					JsonObject jeWin = parser.parse(new String(body)).getAsJsonObject();
					String playerWinner = jeWin.get("playerWinner").getAsString();
					System.out.println(playerWinner + " has won."); //suite parse
					//TODO: View
					break;
				}
	    }
	}
}
