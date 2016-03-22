package distribully.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ClientList extends ConnectingComponent implements IObservable {
	private ArrayList<Player> players;
	//list of observers
	ArrayList<IObserver> observers = new ArrayList<IObserver>();
	
	public ClientList(String serverAddress, int serverPort) {
		super(serverAddress,serverPort);
		players = new ArrayList<Player>();
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	public void deleteFromServer(String playerName){//TODO moet naar player?
		HttpClient client = new HttpClient();
		try {
			client.start();
			client.newRequest(this.serverAddress + ":" + this.serverPort + "/players/" + playerName).method(HttpMethod.DELETE).send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//TODO: Handle response?
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

	
	/**
	 * Fills the list with the game players on the server of the game owned by hostName
	 * @param hostName
	 */
	public boolean fillWithGamePlayers(String hostName) {
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		try {
			client.start();
			response = client.newRequest(this.serverAddress + ":" + this.serverPort + "/game/" + hostName)
					.method(HttpMethod.GET)
					.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			client.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (response.getStatus() == 200) {
			JsonParser jsonParser = new JsonParser();
			//System.out.println(response.getContentAsString());
			JsonElement je = jsonParser.parse(response.getContentAsString());
			JsonArray ja = je.getAsJsonArray();
			
			Gson gson = new Gson();
			Player[] players = gson.fromJson(ja, Player[].class);
			ArrayList<Player> playerList  = new ArrayList<Player>(Arrays.asList(players));
			System.out.println("PL:"+playerList);
			//only update the list if it's different
			boolean listsAreEqual = this.playersListEquals(playerList);
			if (!listsAreEqual) {
				this.players.removeAll(this.players);
				this.players.addAll(playerList);
				this.notifyObservers(this);
			} 

			return true;
		} else if(response.getStatus() == 403){
			this.players.removeAll(this.players);
			return false;
		} else{
			return false;
		}
	}
	
	/**
	 * Adds the player to this clientlist and to the game player server list
	 * @param player
	 */
	public void addGamePlayer(Player player, String hostName) {
		
		
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		
		Gson gson = new Gson();
		
		try {
			client.start();
			response = client.newRequest(this.serverAddress + ":" + this.serverPort + "/game/" + hostName)
					.method(HttpMethod.POST)
					.param("player",gson.toJson(player))
					.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (response.getStatus() == 201) {
			this.players.add(player);
			this.notifyObservers(this);
		} else {
			//TODO peniek!
		}
	}
	
	/**
	 * creates a list on the server under this hostName. Does not modify any instance of ClientList.
	 * @param hostName
	 */
	public void createGameList(Player host) {
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		
		Gson gson = new Gson();
		
		try {
			client.start();
			response = client.newRequest(this.serverAddress + ":" + this.serverPort + "/createGame/" + host.getName())
					.method(HttpMethod.POST)
					.param("player",gson.toJson(host))
					.send();
			client.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (response.getStatus() == 200) {

		} else {
			//TODO peniek!
		}
		
		
	}
	
	public void deleteGameList(String hostName) {
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		
		try {
			client.start();
			response = client.newRequest(this.serverAddress + ":" + this.serverPort + "/endGame/" + hostName)
					.method(HttpMethod.DELETE)
					.send();
			client.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (response.getStatus() == 200) {

		} else {
			//TODO peniek!
		}
		
		
	}
	
	/**
	 * overwrites the current player list with the received playerList
	 * @param playerList
	 */
	public void setPlayers(ArrayList<Player> playerList) {
		if (!this.playersListEquals(playerList)) {
			this.players.removeAll(players);
			playerList.forEach(player -> this.players.add(player));
			
			//System.out.println("updated list of players:" + players.size() + "," + observers.size());
			this.notifyObservers(this);
		}
	}
	
	public Player getPlayerByNickname(String nickname) {
		for (Player p : this.players) {
			if (p.getName().equals(nickname)) {
				return p;
			}
		}
		System.out.println(nickname);
		return null;
	}



	public void deleteFromGame(String playerName, String hostName) {
		HttpClient client = new HttpClient();
		try {
			client.start();
			client.newRequest(this.serverAddress + ":" + this.serverPort + "/game/" + hostName).method(HttpMethod.DELETE).param("playerName",playerName).send();
			client.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//TODO: Handle response?
		
	}
	
	public void removePlayerByPlayerName(String playerName) {
		getPlayers().removeIf(player -> player.getName().equals(playerName));
		notifyObservers(this);
	}

	public boolean playersListEquals(ArrayList<Player> otherPlayerList) {
		Collections.sort(otherPlayerList, new PlayerComperator());
		
		if (this.players.size() != otherPlayerList.size()) {
			return false;
		}
		
		//check if current list and new list are not equal
		boolean listsAreEqual = true;
		for (int i = 0; i < this.players.size(); i++) {
			listsAreEqual = listsAreEqual && (this.players.get(i).equals(otherPlayerList.get(i)));
		}
		
		return listsAreEqual;
	}
	
	public void setPlayerReadyState(String nickname, boolean readyToPlay) {
		this.getPlayerByNickname(nickname).setReadyToPlay(readyToPlay);
		this.notifyObservers(this);
	}
}
