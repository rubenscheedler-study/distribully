package distribully.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import distribully.model.Card;
import distribully.model.CardSuit;
import distribully.model.DistribullyModel;
import distribully.model.Player;
import distribully.model.TurnState;

public class GameConsumerThread{

	private DistribullyModel model;
	private static Logger logger;
	private String queueName;
	public GameConsumerThread(DistribullyModel model){
		logger = LoggerFactory.getLogger("controller.GameConsumerThread");
		this.model = model;
		new ClientListUpdateHandler(model); //Ensure the playerList is up to date
		queueName = model.getNickname();
		String hostName = model.getCurrentHostName();
		Player host = model.getOnlinePlayerList().getPlayerByNickname(hostName);
		if (host == null) { //Host aborted his game before we accepted the invite
			JOptionPane.showMessageDialog(null,
				    hostName + " aborted the game that you were invited for. \n",
				    "No game to join",
				    JOptionPane.WARNING_MESSAGE);
			logger.error("host left before accept");
			new BackToMainPageHandler(model);
			return;
		}
		initPlayerExchange(host);
	}

	private void initPlayerExchange(Player player){ //Open a connection to a specified player
		ConnectionFactory factory = new ConnectionFactory();  
		String playerName = player.getName();
		factory.setHost(player.getIp());
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(queueName, false, false, false, null);
			channel.exchangeDeclare(playerName, "fanout");
			channel.queueBind(queueName, playerName, "");
			channel.queuePurge(queueName); //Empty the queue from message that may remain from a previous game
			Consumer consumer = new MessageConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			logger.info("host connected: " + playerName);
		} catch (TimeoutException | IOException e) {
			//Player has lost internet availability or rabbitMQ is not running -> remove from playerList
			model.getGamePlayerList().getPlayers().remove(player);
			logger.error("Something went wrong when connecting to host "+ playerName +".");
		}
	}

	class MessageConsumer extends DefaultConsumer{ //Custom consumer
		JsonParser parser;
		Gson gson;
		public MessageConsumer(Channel channel) {
			super(channel);
			parser = new JsonParser();
			gson = new Gson();
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope,
				AMQP.BasicProperties properties, byte[] body) throws IOException {
			switch(envelope.getRoutingKey()){ //Use key to identify the type of message
			case "Start":
				handleStart();
				break;
			case "Leave":
				handleLeave(new String(body));
				break;
			case "Rules":
				handleRules(new String(body));
				break;
			case "PlayCard":
				handleCard(new String(body));
				break;
			case "NextTurn":
				handleNextTurn(new String(body));
				break;
			case "MustDraw":
				handleMustDraw(new String(body));
				break;
			case "ChooseSuit":
				handleChooseSuit(new String(body));
				break;
			case "InitStack":
				handleInitStack(new String(body));
				break;
			case "Win":
				handleWin(new String(body));
				break;
			}
		}

		private void handleWin(String body) {//Tell everyone a certain player has won
			JsonObject jo = parser.parse(body).getAsJsonObject();
			String playerWinner = jo.get("playerWinner").getAsString();
			logger.info(playerWinner + " has won.");
			JOptionPane.showMessageDialog(null,
				    playerWinner + " has won!",
				    "Game over",
				    JOptionPane.PLAIN_MESSAGE);

			new BackToMainPageHandler(model);	
		}

		private void handleInitStack(String body) { //Set a top of stack for a player
			JsonObject jo = parser.parse(body).getAsJsonObject();
			int cardId = Integer.parseInt(jo.get("cardId").getAsString());
			int suit = Integer.parseInt(jo.get("cardSuit").getAsString());
			String playerName = jo.get("playerName").getAsString();
			logger.info(playerName +" has top of stack " + suit + " " + cardId);
			model.putTopOfStack(model.getGamePlayerList().getPlayerByNickname(playerName), new Card(cardId, CardSuit.values()[suit]));
		}

		private void handleChooseSuit(String body) { //Update the suit of a top of stack
			JsonObject jo = parser.parse(body).getAsJsonObject();
			int suit = Integer.parseInt(jo.get("cardSuit").getAsString());
			JsonObject turnState = jo.get("turnState").getAsJsonObject();
			TurnState newState = gson.fromJson(turnState, TurnState.class);
			logger.info("new suite on is " + suit); //suite parse
			model.getTopOfStacks().get(model.getGamePlayerList().getPlayerByNickname(newState.getLastStack())).setSuit(CardSuit.values()[suit]);
			model.setTurnState(newState);
		}

		private void handleMustDraw(String body) {
			JsonObject jo= parser.parse(body).getAsJsonObject(); 
			int drawAmount = Integer.parseInt(jo.get("drawAmount").getAsString());
			JsonObject turnState = jo.get("turnState").getAsJsonObject();
			TurnState nextState = gson.fromJson(turnState, TurnState.class); //The state that will be used after the user has drawn cards
			logger.info("Must draw " + drawAmount + " cards");
			if(model.isMyTurn()){ //Must draw cards
				new BroadcastDrawHandler(model, drawAmount, nextState);
			}	
		}

		private void handleNextTurn(String body) {
			if(model.isReadyToWin()){ //If we get a next turn and not a must draw, we have won.
				new BroadcastWinHandler(model);
			}
			JsonObject jo = parser.parse(body).getAsJsonObject();
			JsonObject turnState = jo.get("turnState").getAsJsonObject();
			TurnState newState = gson.fromJson(turnState, TurnState.class);
			model.setTurnState(newState);

			logger.info("Next player is "+ newState.getNextPlayer() +" by action " + newState.getAction());
			if(model.isMyTurn()){
				if (newState.isChooseSuit()){
					String suitCandidate = "";
					int cardSuitIndex = -1;
					while (suitCandidate.equals("")) {
						suitCandidate = JOptionPane.showInputDialog(null,
								"Choose a new suit for this stack from: \n"
										+ "hearts, diamonds, clubs, spades",
										"Choose a New Suit",
										JOptionPane.QUESTION_MESSAGE).toLowerCase();
						switch (suitCandidate) {
						case "hearts":
							cardSuitIndex = 0;
							break;
						case "diamonds" :
							cardSuitIndex = 1;
							break;
						case "clubs":
							cardSuitIndex = 2;
							break;
						case "spades":
							cardSuitIndex = 3;
							break;
						default:
							suitCandidate = "";
							break;
						}
					}
					new BroadcastSuitHandler(model, cardSuitIndex);
				}
			}		
		}

		private void handleCard(String body) {

			JsonObject jo = parser.parse(body).getAsJsonObject();
			int cardId = Integer.parseInt(jo.get("cardId").getAsString());
			int suiteId = Integer.parseInt(jo.get("suitId").getAsString());
			String stackOwner = jo.get("stackOwner").getAsString();
			logger.info("Card "+ cardId +" played");
			if(stackOwner.equals(model.getNickname())){ //Played on my stack
				new ExecuteCardHandler(model, cardId);
			}
			model.putTopOfStack(model.getGamePlayerList().getPlayerByNickname(stackOwner),new Card(cardId, CardSuit.values()[suiteId]));
		}

		private void handleRules(String body) {
			JsonElement je = parser.parse(body);
			String playerName= je.getAsJsonObject().get("playerName").getAsString();
			logger.info("Rules from  "+ playerName + " received");
			Player player = model.getGamePlayerList().getPlayerByNickname(playerName);
			player.setReadyToPlay(true);
			model.getGamePlayerList().setPlayerReadyState(playerName,true);
			if(model.getGamePlayerList().getPlayers().stream().allMatch(p->p.isReadyToPlay() && model.getGAME_STATE() == GameState.WAITING_FOR_GAMESTART)){
				handleReady(); //If everyone is ready and user is waiting for everyone to be ready
			}
		}

		private void handleReady() {
			new BroadcastTopOfStackHandler(model);
			model.generateNewHand();
			//Only one player may create the first turn object. We define this as the alphabetically first player
			ArrayList<Player> toSort = new ArrayList<Player>();
			toSort.addAll(model.getGamePlayerList().getPlayers());
			Collections.sort(toSort, (p1, p2) -> p1.getName().compareTo(p2.getName()));
			if (model.getNickname().equals(toSort.get(0).getName())) { //Can only get here when there are players, so 0 always exists.
				new BroadcastFirstTurnHandler(model);
			}
			model.setGAME_STATE(GameState.IN_GAME);
		}

		private void handleLeave(String body) {
			JsonElement je = parser.parse(body);
			String playerName = je.getAsJsonObject().get("playerName").getAsString();
			logger.info(playerName + " left");
			JsonObject jo = parser.parse(body).getAsJsonObject();
			JsonObject turnState = jo.get("turnState").getAsJsonObject();
			TurnState newState = gson.fromJson(turnState, TurnState.class);
			model.setTurnState(newState);//Ensure the setUpdate happens before the remove, to prevent async errors
			model.getGamePlayerList().removePlayerByPlayerName(playerName);
			if(model.getGamePlayerList().getPlayers().stream().allMatch(p->p.isReadyToPlay()) && model.getGAME_STATE() == GameState.WAITING_FOR_GAMESTART){
				handleReady(); //If everyone is ready and user is waiting for everyone to be ready
			}
			if(model.getGamePlayerList().getPlayers().size() <= 1){ //Solo players always win
				if(model.getGamePlayerList().getPlayers().stream().anyMatch(p -> p.getName().equals(model.getNickname()))){
					JOptionPane.showMessageDialog(null,
						    "You have won!",
						    "Game over",
						    JOptionPane.PLAIN_MESSAGE);
					model.getInviteStates().clear();
					new BackToMainPageHandler(model);	
				}
			}
		}

		private void handleStart() {
			logger.info("Game is starting!");
			if(DistribullyController.lobbyThread != null){
				DistribullyController.lobbyThread.setInLobby(false);
			}
			new ClientListUpdateHandler(model);
			
			model.getInviteStates().clear();
			model.getChosenRules().clear();
			
			model.getGamePlayerList().getPlayers().forEach(player -> initPlayerExchange(player)); //No need to exclude the host, since it will just use the earlier existing exchange
			model.setGAME_STATE(GameState.SETTING_RULES);			
		}
	}
}
