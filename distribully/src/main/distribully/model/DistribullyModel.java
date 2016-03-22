package distribully.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.controller.GameState;
import distribully.controller.ProducerHandler;
import distribully.model.rules.ChooseSuitRule;
import distribully.model.rules.DrawFiveRule;
import distribully.model.rules.DrawTwoRule;
import distribully.model.rules.PlayAgainRule;
import distribully.model.rules.RevertTurnOrderRule;
import distribully.model.rules.Rule;
import distribully.model.rules.SkipTurnRule;

public class DistribullyModel implements IObservable {
	private ClientList onlinePlayerList;//Contains the current list of online players copied from the server

	private ClientList gamePlayerList;//Contains the players that are part of the game that this user is a part of.

	private GameState GAME_STATE;
	
	private String serverAddress = "http://localhost"; //Hardcoded for testing, does also work with ip's if the port is open
	private final int serverPort = 4567;
	private String myIP;
	private String currentHostName;
	private int myPort;
	private Gson gson;
	private JsonParser parser;

	private ArrayList<IObserver> observers;
	private HashMap<String,String> inviteStates;

	private String nickname;
	private HashMap<Player,Card> topOfStacks;
	private List<Card> hand;

	private ArrayList<Rule> allRules;
	private HashMap<Integer,Rule> chosenRules;

	private TurnState turnState;

	private boolean isReadyToWin;

	public DistribullyModel() {
		this.onlinePlayerList = new ClientList(serverAddress,serverPort);
		this.gamePlayerList = new ClientList(serverAddress, serverPort);
		observers = new ArrayList<IObserver>();
		inviteStates = new HashMap<String,String>();
		allRules = new ArrayList<Rule>();
		chosenRules = new HashMap<Integer,Rule>();
		fillAllRules();
		hand = Collections.synchronizedList(new ArrayList<Card>()); //Synchronize to prevent concurrency issues
		topOfStacks = new HashMap<Player,Card>();
		gson = new Gson();
		parser = new JsonParser();
		setReadyToWin(false);
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

	/*
	 * Checks for all entries in the hashmap if the server still contains it in the game players. Drops it if not.
	 */
	public void updateInviteStatesByListState(ClientList gamePlayers) {
		ArrayList<String> toRemove = new ArrayList<String>();
		//Check which players left
		for (String name : this.inviteStates.keySet()) {
			if (this.inviteStates.get(name).equals("Accepted") && gamePlayers.getPlayerByNickname(name) == null) {
				toRemove.add(name);
			}
		}
		//Remove them from the invitation states
		toRemove.forEach(name -> this.inviteStates.remove(name));

		if (!toRemove.isEmpty()) {
			this.notifyObservers(this.inviteStates);
		}
	}

	public void setAndBroadCastTopOfStack() { //Randomly generate a top of stack for yourself
		Card card = Card.getARandomCard();

		JsonObject message = new JsonObject();
		message.addProperty("cardId", card.getNumber());
		message.addProperty("cardSuit", card.getSuit().getV());
		message.addProperty("playerName", this.getNickname());
		new ProducerHandler(message.toString(), "InitStack" ,this.getMe());
	}

	public void generateNewHand() { //Generate a clean hand of 7 cards, as seven is the default in pesten
		for (int i = 0; i < 7; i++) {
			this.hand.add(Card.getARandomCard());
		}
		this.notifyObservers(this);
	}

	public void generateAndSendFirstTurnObject() { //Choose and broadcast who gets the first turn
		int i = (int)(Math.random()*(double)this.gamePlayerList.getPlayers().size());
		String nextPlayer = this.gamePlayerList.getPlayers().get(i).getName();
		int direction = 1;
		int toPick = 0;
		TurnState turnState = new TurnState(nextPlayer,toPick,direction, "", false, "");

		JsonObject message = new JsonObject();
		message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

		new ProducerHandler(message.toString(),"NextTurn" ,this.getMe());
	}

	public void executeCard(int cardId) {
		int direction = 1;
		int toPick = 0;
		TurnState turnState;
		if(chosenRules.containsKey(cardId)){ //Special rule
			Rule rule = chosenRules.get(cardId);
			turnState = rule.execute();
		}else{
			direction = this.getTurnState().getDirection();
			toPick = this.getTurnState().getToPick();
			turnState = new TurnState(getNextPlayer(),toPick,direction, "", false,this.getNickname());
		}
		if(turnState.getToPick() == this.getTurnState().getToPick() && turnState.getToPick() > 0){ //Number of card to pick did not increase, tell user to draw them
			turnState.setToPick(0);
			JsonObject message = new JsonObject();
			message.addProperty("drawAmount",  this.getTurnState().getToPick());
			message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

			new ProducerHandler(message.toString(),"MustDraw" ,this.getMe());
		}
		else{
			JsonObject message = new JsonObject();
			message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());

			new ProducerHandler(message.toString(), "NextTurn" ,this.getMe());
		}
	}



	public void draw(int drawAmount, TurnState nextTurn) {
		isReadyToWin = false;

		//Draw the cards, add them to the hand
		for (int i = 0; i < drawAmount; i++) {
			hand.add(Card.getARandomCard());
		}

		//Notify others about what this player has done
		JsonObject message = new JsonObject();
		message.add("turnState",  parser.parse((gson.toJson(nextTurn))).getAsJsonObject());

		new ProducerHandler(message.toString(), "NextTurn" ,this.getMe());

	}

	public String getNextPlayer() { //Get the player who's turn is next
		int numPlayers = this.gamePlayerList.getPlayers().size();
		int index;
		if(this.getTurnState() != null){
			index = this.gamePlayerList.getPlayers().indexOf(gamePlayerList.getPlayerByNickname(this.getTurnState().getNextPlayer()));
			return this.gamePlayerList.getPlayers().get((index + numPlayers + getTurnState().getDirection()) % numPlayers ).getName(); //Ensure we stay in the range
		} else{
			//Just return any player since the game hasn't started yet and the start player is yet to be set.
			return this.gamePlayerList.getPlayers().get((numPlayers + 1) % numPlayers ).getName(); //Ensure we stay in the range
		}

	}

	public void broadcastStackSuit(int cardSuitIndex) {//Tell everyone which suit you picked
		TurnState turnState = new TurnState(getNextPlayer(),this.turnState.getToPick(),this.turnState.getDirection(),this.getNickname() + " changed the suit of the stack of " + this.getTurnState().getLastStack() + ".", false, this.getTurnState().getLastStack());

		JsonObject message = new JsonObject();

		message.add("turnState",  parser.parse((gson.toJson(turnState))).getAsJsonObject());
		message.addProperty("cardSuit", cardSuitIndex);

		new ProducerHandler(message.toString(), "ChooseSuit",this.getMe());
	}

	public void broadcastWin() { //Tell everyone you won
			JsonObject message = new JsonObject();
			message.addProperty("playerWinner", getNickname());

			new ProducerHandler(message.toString(), "Win",this.getMe());
	}

	public boolean isMyTurn() {
		return this.getTurnState() != null && this.getNickname().equals(this.getTurnState().getNextPlayer());
	}
	
	public boolean isReadyToWin() {
		return isReadyToWin;
	}

	public void setReadyToWin(boolean isReadyToWin) {
		this.isReadyToWin = isReadyToWin;
	}
	
	public TurnState getTurnState() {
		return turnState;
	}

	public void setTurnState(TurnState turnState) {
		this.turnState = turnState;
		this.notifyObservers(this.turnState);
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

	public List<Card> getHand() {
		return hand;
	}
	
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

	public void setGAME_STATE(GameState gameState) {
		if (GAME_STATE != gameState) {
			GAME_STATE = gameState;
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
		this.observers.forEach(observer -> observer.update(this, changedObject));
	}
}
