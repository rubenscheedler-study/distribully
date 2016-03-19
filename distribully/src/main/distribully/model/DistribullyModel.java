package distribully.model;

import java.util.ArrayList;
import java.util.HashMap;

import distribully.controller.GameState;

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
	
	
	public DistribullyModel() {
		this.onlinePlayerList = new ClientList(serverAddress,serverPort);
		this.gamePlayerList = new ClientList(serverAddress, serverPort);
		observers = new ArrayList<IObserver>();
		inviteStates = new HashMap<String,String>();
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
		GAME_STATE = gAME_STATE;
		this.notifyObservers();
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
		this.notifyObservers();
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
	public void notifyObservers() {
		//System.out.println("notifying model observers (count=" + this.observers.size() + ")");
		this.observers.forEach(observer -> observer.update(this));
	}
	
	public HashMap<String, String> getInviteStates() {
		return inviteStates;
	}

	public void setInviteStates(HashMap<String, String> inviteStates) {
		this.inviteStates = inviteStates;
	}
	
	public void putInviteState(String key, String inviteState) {
		this.inviteStates.put(key, inviteState);
		this.notifyObservers();
	}

	public String getCurrentHostName() {
		return currentHostName;
	}

	public void setCurrentHostName(String currentHostName) {
		this.currentHostName = currentHostName;
	}


}
