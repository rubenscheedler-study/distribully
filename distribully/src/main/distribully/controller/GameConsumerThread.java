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

import distribully.model.Card;
import distribully.model.CardSuit;
import distribully.model.DistribullyModel;
import distribully.model.Player;

public class GameConsumerThread extends Thread{

	DistribullyModel model;

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
		String queueName = model.getNickname();
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			if(model.getCurrentHostName().equals(model.getNickname()) && model.getNickname().equals(playerName)){
				//channel.queueDelete(queueName);
			}
			channel.queueDeclare(queueName, false, false, false, null);
			channel.exchangeDeclare(playerName, "fanout");
			channel.queueBind(queueName, playerName, "");
			Consumer consumer = new MessageConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			System.out.println("host connected: " + playerName);
		} catch (TimeoutException e) {
			//Player has lost internet availability or rabbitMQ is not running -> remove from playerList
			model.getGamePlayerList().getPlayers().remove(player);
		} catch (IOException e) {
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
					if(model.getGamePlayerList().getPlayers().size() <= 1){
						if(model.getGamePlayerList().getPlayers().stream().anyMatch(p -> p.getName() == model.getNickname())){
							//YOU WON //TODO: Dit iets automatischer maken, niet alleen bij leave, mss bij elke refresh/actie?
						}
						//Redirect main screen, reboot al die threads enzo
					}
					break;
				case "Rules":
					JsonElement jeRule = parser.parse(new String(body));
					String playerNameRule = jeRule.getAsJsonObject().get("playerName").getAsString();
					System.out.println("Rules from  "+ playerNameRule + " received");
					Player player = model.getGamePlayerList().getPlayerByNickname(playerNameRule);
					player.setReadyToPlay(true);
					if(model.getGamePlayerList().getPlayers().stream().allMatch(p->p.isReadyToPlay())){
						model.setGAME_STATE(GameState.IN_GAME);
						model.setAndBroadCastTopOfStack();
					}
					break;
				case "PlayCard":
					JsonObject jeCard = parser.parse(new String(body)).getAsJsonObject();
					int cardId = Integer.parseInt(jeCard.get("cardId").getAsString());
					int cardSuit = Integer.parseInt(jeCard.get("cardSuit").getAsString());
					String playerName = jeCard.get("playerName").getAsString();
					System.out.println("Card "+ cardId + " from suite " + cardSuit +" played on stack of "+ playerName); //TODO cardSuite parser
					//TODO: View
					break;
				case "NextTurn":
					JsonObject jeTurn = parser.parse(new String(body)).getAsJsonObject();
					String action = jeTurn.get("action").getAsString();
					String playerNameNext = jeTurn.get("playerName").getAsString();
					System.out.println("Next player is "+ playerNameNext +" by action " + action);
					//TODO: View
					break;
				case "ChooseSuit":
					JsonObject jeSuit = parser.parse(new String(body)).getAsJsonObject();
					int suit = Integer.parseInt(jeSuit.get("cardSuit").getAsString());
					String playerNext = jeSuit.get("playerNextName").getAsString();
					String playerCurrent = jeSuit.get("playerName").getAsString();
					System.out.println("Next player is "+ playerNext +", new suite on "+ playerCurrent +" is " + suit); //suite parse
					//TODO: View
					break;
				case "InitStack":
					JsonObject jeStack = parser.parse(new String(body)).getAsJsonObject();
					int stackCardId = Integer.parseInt(jeStack.get("cardId").getAsString());
					int stackSuit = Integer.parseInt(jeStack.get("cardSuit").getAsString());
					String playerStackName = jeStack.get("playerName").getAsString();
					System.out.println(playerStackName +" has top of stack " + stackSuit + " " + stackCardId);
					//TODO: view
					model.putTopOfStack(model.getGamePlayerList().getPlayerByNickname(playerStackName), new Card(stackCardId, CardSuit.values()[stackSuit]));
					break;
				case "Win":
					JsonObject jeWin = parser.parse(new String(body)).getAsJsonObject();
					String playerWinner = jeWin.get("playerWinner").getAsString();
					System.out.println(playerWinner + " has won.");
					//TODO: popup
					//TODO: Reset Hashmap of responses
					//TODO: Back to main (threads, gamestate)
					
					break;
				}
	    }
	}
}
