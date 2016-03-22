package distribully.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.controller.GameState;
import distribully.model.rules.ChooseSuitRule;
import distribully.model.rules.DrawFiveRule;
import distribully.model.rules.DrawTwoRule;
import distribully.model.rules.PlayAgainRule;
import distribully.model.rules.RevertTurnOrderRule;
import distribully.model.rules.Rule;
import distribully.model.rules.SkipTurnRule;

public class DistribullyModel implements IObservable {
	private ClientList onlinePlayerList;//contains the current list of online players copied from the server

	private ClientList gamePlayerList;//contains the players that are part of the game that this user is a part of.

	private String serverAddress = "http://82.73.233.237";//82.73.233.237/82.72.30.166
	private final int serverPort = 4567;
	private String myIP;
	private String currentHostName;
	private int myPort;

	private ArrayList<IObserver> observers;
	private HashMap<String,String> inviteStates;

	private String nickname;
	private Stack stack;
	private HashMap<Player,Card> topOfStacks;
	private ArrayList<Card> hand;

	private ArrayList<Rule> allRules;
	private HashMap<Integer,Rule> chosenRules;

	private TurnState turnState;

	public DistribullyModel() {
		this.stack = new Stack();
		this.onlinePlayerList = new ClientList(serverAddress,serverPort);
		this.gamePlayerList = new ClientList(serverAddress, serverPort);
		observers = new ArrayList<IObserver>();
		inviteStates = new HashMap<String,String>();
		allRules = new ArrayList<Rule>();
		chosenRules = new HashMap<Integer,Rule>();
		fillAllRules();
		hand = new ArrayList<Card>();
		topOfStacks = new HashMap<Player,Card>();

	}



	private void fillAllRules() {
		allRules.add(new DrawTwoRule(this));
		allRules.add(new SkipTurnRule(this));
		allRules.add(new ChooseSuitRule(this));
		allRules.add(new DrawFiveRule(this));
		allRules.add(new PlayAgainRule(this));
		allRules.add(new RevertTurnOrderRule(this));
		Collections.sort(allRules, (rule1, rule2) -> rule1.toString().compareTo(rule2.toString()));
	}



	private GameState GAME_STATE;

	/**
	 * finds the player object corresponding with the user.
	 * @return
	 */
	public Player getMe() {
		return this.onlinePlayerList.getPlayerByNickname(nickname);
	}

	public String getMyIP() {
		return myIP;
	}

	public void setMyIP(String myIP) {
		this.myIP = myIP;
	}

	public int getMyPort() {
		return myPort;
	}

	public void setMyPort(int myPort) {
		this.myPort = myPort;
	}

	public GameState getGAME_STATE() {
		return GAME_STATE;
	}

	public void setGAME_STATE(GameState gAME_STATE) {
		if (GAME_STATE != gAME_STATE) {
			GAME_STATE = gAME_STATE;
			this.notifyObservers(GAME_STATE);
		}
	}

	public ClientList getGamePlayerList() {
		return gamePlayerList;
	}

	public void setGamePlayerList(ClientList gamePlayerList) {
		this.gamePlayerList = gamePlayerList;
	}

	public ClientList getOnlinePlayerList() {
		return onlinePlayerList;
	}

	public void setOnlinePlayerList(ClientList onlinePlayerList) {
		this.onlinePlayerList = onlinePlayerList;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public void addObserver(IObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(IObserver observer) {
		this.observers.remove(observer);
	}

	@Override
	public void notifyObservers(Object changedObject) {
		//System.out.println("notifying model observers (count=" + this.observers.size() + ")");
		this.observers.forEach(observer -> observer.update(this, changedObject));
	}

	public HashMap<String, String> getInviteStates() {
		return inviteStates;
	}

	public void setInviteStates(HashMap<String, String> inviteStates) {
		this.inviteStates = inviteStates;
	}

	public void putInviteState(String key, String inviteState) {
		if (!this.inviteStates.containsKey(key) || !this.inviteStates.get(key).equals(inviteState)) {
			this.inviteStates.put(key, inviteState);
			this.notifyObservers(this.inviteStates);
		}
	}

	/**
	 * checks for all entries in the hashmap if the server still contains it in the game players. Drops it, if not.
	 * TODO add NEW players as well?
	 * @param gamePlayers
	 */
	public void updateInviteStatesByListState(ClientList gamePlayers) {
		ArrayList<String> toRemove = new ArrayList<String>();
		//check which players left
		for (String name : this.inviteStates.keySet()) {
			if (this.inviteStates.get(name).equals("Accepted") && gamePlayers.getPlayerByNickname(name) == null) {
				toRemove.add(name);
			}
		}
		//remove them from the invitation states
		toRemove.forEach(name -> this.inviteStates.remove(name));


		if (!toRemove.isEmpty()) {
			this.notifyObservers(this.inviteStates);
		}
	}

	public String getCurrentHostName() {
		return currentHostName;
	}

	public void setCurrentHostName(String currentHostName) {
		this.currentHostName = currentHostName;
	}

	public void setCardRule(int cardNumber, Rule rule) {
		this.chosenRules.put(cardNumber, rule);
		this.notifyObservers(this.chosenRules);
	}

	public void removeCardRule(int cardNumber) {
		this.chosenRules.remove(cardNumber);
		this.notifyObservers(this.chosenRules);
	}

	public ArrayList<Rule> getAllRules() {
		return allRules;
	}

	public void setAllRules(ArrayList<Rule> allRules) {
		this.allRules = allRules;
	}

	public HashMap<Integer,Rule> getChoosenRules() {
		return chosenRules;
	}

	public void setChoosenRules(HashMap<Integer,Rule> choosenRules) {
		this.chosenRules = choosenRules;
	}



	public HashMap<Player,Card> getTopOfStacks() {
		return topOfStacks;
	}



	public void setTopOfStacks(HashMap<Player,Card> topOfStacks) {
		this.topOfStacks = topOfStacks;
	}

	public void putTopOfStack(Player player, Card card) {
		this.topOfStacks.put(player, card);
		notifyObservers(this);
	}

	public ArrayList<Card> getHand() {
		return hand;
	}



	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}



	public void setAndBroadCastTopOfStack() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(this.getMe().getIp());
		Connection connection;
		try {
			connection = factory.newConnection();

			Channel channel = connection.createChannel();

			channel.exchangeDeclare(this.getNickname(), "fanout");

			Card card = Card.getARandomCard();

			JsonObject message = new JsonObject();
			message.addProperty("cardId", card.getNumber());
			message.addProperty("cardSuit", card.getSuit().getV());
			message.addProperty("playerName", this.getNickname());

			channel.basicPublish(this.getNickname(), "InitStack", null, message.toString().getBytes());
			System.out.println(" [x] Sent '" + message + "'");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}



	public void generateNewHand() {
		for (int i = 0; i < 7; i++) {
			this.hand.add(Card.getARandomCard());
		}
		this.notifyObservers(this);
	}



	public TurnState getTurnState() {
		return turnState;
	}



	public void setTurnState(TurnState turnState) {
		this.turnState = turnState;
		this.notifyObservers(this.turnState);
	}



	public void generateAndSendFirstTurnObject() {
		int i = (int)(Math.random()*(double)this.gamePlayerList.getPlayers().size());
		String nextPlayer = this.gamePlayerList.getPlayers().get(i).getName();
		int direction = 1;
		int toPick = 0;
		TurnState turnState = new TurnState(nextPlayer,toPick,direction, "");

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(this.getMe().getIp());
		Connection connection;
		try {
			connection = factory.newConnection();

			Channel channel = connection.createChannel();

			channel.exchangeDeclare(this.getNickname(), "fanout");

			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject message = new JsonObject();
			message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

			channel.basicPublish(this.getNickname(), "NextTurn", null, message.toString().getBytes());
			System.out.println(" [x] Sent '" + message + "'");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}



	public void executeCard(int cardId) {
		int direction = 1;
		int toPick = 0;
		String action;
		TurnState turnState;
		if(chosenRules.containsKey(cardId)){
			Rule rule = chosenRules.get(cardId);
			turnState = rule.execute();
		}else{
			direction = this.getTurnState().getDirection();
			toPick = this.getTurnState().getToPick();
			turnState = new TurnState(getNextPlayer(),toPick,direction, "");

		}
		if(turnState.getToPick() == this.getTurnState().getToPick() && turnState.getToPick() > 0){
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(this.getMe().getIp());
			Connection connection;
			try {
				connection = factory.newConnection();

				Channel channel = connection.createChannel();

				channel.exchangeDeclare(this.getNickname(), "fanout");
				JsonObject message = new JsonObject();
				message.addProperty("drawAmount",  this.getTurnState().getToPick());
				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());


				channel.basicPublish(this.getNickname(), "MustDraw", null, message.toString().getBytes());
				System.out.println(" [x] Sent '" + message + "'");

				channel.close();
				connection.close();
			} catch (IOException | TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(this.getMe().getIp());
			Connection connection;
			try {
				connection = factory.newConnection();

				Channel channel = connection.createChannel();

				channel.exchangeDeclare(this.getNickname(), "fanout");

				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				JsonObject message = new JsonObject();
				message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

				channel.basicPublish(this.getNickname(), "NextTurn", null, message.toString().getBytes());
				System.out.println(" [x] Sent '" + message + "'");

				channel.close();
				connection.close();
			} catch (IOException | TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}


	}

	public boolean isMyTurn() {
		return this.getTurnState() != null && this.getNickname().equals(this.getTurnState().getNextPlayer());
	}



	public void draw(int drawAmount, TurnState nextTurn) {
		//update model according to action
		//draw the cards, add them to the hand
		for (int i = 0; i < drawAmount; i++) {
			hand.add(Card.getARandomCard());
		}

		//notify others about what this player has done
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(this.getMe().getIp());
		Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.exchangeDeclare(this.getNickname(), "fanout");

			Gson gson = new Gson();
			JsonParser parser = new JsonParser();

			JsonObject message = new JsonObject();

			message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

			message.addProperty("amount", drawAmount);

			channel.basicPublish(this.getNickname(), "HaveDrawn", null, message.toString().getBytes());
			System.out.println(" [x] Sent '" + message + "'");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}



	public String getNextPlayer() {
		int numPlayers = this.gamePlayerList.getPlayers().size();
		int index = this.gamePlayerList.getPlayers().indexOf(gamePlayerList.getPlayerByNickname(this.getTurnState().getNextPlayer()));
		return this.gamePlayerList.getPlayers().get((index + numPlayers + getTurnState().getDirection()) % numPlayers ).getName(); //Ensure we stay in the range
	}



	public void broadcastStackSuit(String stackOwner, int cardSuitIndex) {
		
		TurnState turnState = new TurnState(getNextPlayer(),this.turnState.getToPick(),this.turnState.getDirection(),this.getNickname() + " changed the suit of the stack of " + stackOwner + ".");
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(this.getMe().getIp());
		Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.exchangeDeclare(this.getNickname(), "fanout");

			Gson gson = new Gson();
			JsonParser parser = new JsonParser();

			JsonObject message = new JsonObject();

			message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

			message.addProperty("cardSuit", cardSuitIndex);
			message.addProperty("stackPlayer", stackOwner);
			
			channel.basicPublish(this.getNickname(), "ChooseSuit", null, message.toString().getBytes());
			System.out.println(" [x] Sent '" + message + "'");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
