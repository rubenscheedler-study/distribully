package distribully.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.controller.GameState;
import distribully.model.rules.ChooseSuiteRule;
import distribully.model.rules.DrawFiveRule;
import distribully.model.rules.DrawTwoRule;
import distribully.model.rules.PlayAgainRule;
import distribully.model.rules.RevertTurnOrderRule;
import distribully.model.rules.Rule;
import distribully.model.rules.SkipTurnRule;

public class DistribullyModel implements IObservable {
	private ClientList onlinePlayerList;//contains the current list of online players copied from the server

	private ClientList gamePlayerList;//contains the players that are part of the game that this user is a part of.

	private String serverAddress = "http://82.73.233.237";
	private int serverPort = 4567;
	private String myIP;
	private String currentHostName;
	private int myPort = 4567;

	private ArrayList<IObserver> observers;
	private HashMap<String,String> inviteStates;

	private String nickname;
	private Stack stack;
	private HashMap<Player,Card> topOfStacks;
	private ArrayList<Card> hand;

	private ArrayList<Rule> allRules;
	private HashMap<Integer,Rule> choosenRules;

	public DistribullyModel() {
		this.stack = new Stack();
		this.onlinePlayerList = new ClientList(serverAddress,serverPort);
		this.gamePlayerList = new ClientList(serverAddress, serverPort);
		observers = new ArrayList<IObserver>();
		inviteStates = new HashMap<String,String>();
		allRules = new ArrayList<Rule>();
		choosenRules = new HashMap<Integer,Rule>();
		fillAllRules();
		hand = new ArrayList<Card>();
		topOfStacks = new HashMap<Player,Card>();
		this.hand.add(new Card(5,CardSuit.CLUBS));
		this.hand.add(new Card(12,CardSuit.HEARTS));
		this.hand.add(new Card(12,CardSuit.HEARTS));
		this.hand.add(new Card(12,CardSuit.HEARTS));
		this.hand.add(new Card(12,CardSuit.HEARTS));
		this.hand.add(new Card(12,CardSuit.HEARTS));
		
	}



	private void fillAllRules() {
		allRules.add(new DrawTwoRule(this.stack));
		allRules.add(new SkipTurnRule(this.stack));
		allRules.add(new ChooseSuiteRule(this.stack));
		allRules.add(new DrawFiveRule(this.stack));
		allRules.add(new PlayAgainRule(this.stack));
		allRules.add(new RevertTurnOrderRule(this.stack));
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

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
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
		this.choosenRules.put(cardNumber, rule);
		this.notifyObservers(this.choosenRules);
	}

	public void removeCardRule(int cardNumber) {
		this.choosenRules.remove(cardNumber);
		this.notifyObservers(this.choosenRules);
	}

	public ArrayList<Rule> getAllRules() {
		return allRules;
	}

	public void setAllRules(ArrayList<Rule> allRules) {
		this.allRules = allRules;
	}

	public HashMap<Integer,Rule> getChoosenRules() {
		return choosenRules;
	}

	public void setChoosenRules(HashMap<Integer,Rule> choosenRules) {
		this.choosenRules = choosenRules;
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

			channel.basicPublish(this.getNickname(), "TopOfStack", null, message.toString().getBytes());
			System.out.println(" [x] Sent '" + message + "'");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
