package distribully.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import distribully.controller.GameState;
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
	
	private String serverAddress = "http://localhost"; //Hardcoded for testing, does also work with external ip's if the port is open
	private final int serverPort = 4567; //Default port for Spark server
	private String myIP;
	private String currentHostName;
	private int myPort;

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

	public void generateNewHand() { //Generate a clean hand of 7 cards, as seven is the default in pesten
		for (int i = 0; i < 7; i++) {
			this.hand.add(Card.getARandomCard());
		}
		this.notifyObservers(this);
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

	public HashMap<Integer,Rule> getChosenRules() {
		return chosenRules;
	}

	public void setChosenRules(HashMap<Integer,Rule> choosenRules) {
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
