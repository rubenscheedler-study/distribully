package distribully.model;

import distribully.controller.GameState;

public class DistribullyModel {
	private ClientList clientList;//contains the current list of available players copied from the server
	private String serverAddress = "http://82.72.30.166";
	private int serverPort = 4567;
	private String myIP;
	private int myPort = 4567;
	
	private GameState GAME_STATE;
	
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
	}

	public DistribullyModel() {
		this.clientList = new ClientList();
	}

	public ClientList getClientList() {
		return clientList;
	}

	public void setClientList(ClientList clientList) {
		this.clientList = clientList;
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
}
