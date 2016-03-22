package distribully.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ClientList extends ConnectingComponent implements IObservable {
	private ArrayList<Player> players;
	//list of observers
	private ArrayList<IObserver> observers = new ArrayList<IObserver>();
	private static Logger logger;
	
	public ClientList(String serverAddress, int serverPort) {
		super(serverAddress,serverPort);
		players = new ArrayList<Player>();
		logger = LoggerFactory.getLogger("model.ClientList");
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
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
			client.stop();
		} catch (Exception e) {
			logger.error("Something went wrong when sending the request to get the userList.");
			return false;
		}
		
		if (response.getStatus() == 200) {
			JsonParser jsonParser = new JsonParser();
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
			logger.error("Something went wrong when sending the request to add an user.");
			return;
		}
		
		if (response.getStatus() == 201) {
			this.players.add(player);
			this.notifyObservers(this);
		} else {
			logger.error("Something went wrong when adding an user.");
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
			logger.error("Something went wrong when sending the request to create a game.");
			return;
		}
		
		if (response.getStatus() != 200) {
			logger.error("Something went wrong when creating a game.");
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
			logger.error("Something went wrong when sending the request to delete a game.");
			return;
		}
		
		if (response.getStatus() != 200) {
			logger.error("Something went wrong when deleting a game.");
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
			this.notifyObservers(this);
		}
	}
	
	public Player getPlayerByNickname(String nickname) {
		for (Player p : this.players) {
			if (p.getName().equals(nickname)) {
				return p;
			}
		}
		return null;
	}



	public void deleteFromGame(String playerName, String hostName) {
		HttpClient client = new HttpClient();
		ContentResponse response;
		try {
			client.start();
			response = client.newRequest(this.serverAddress + ":" + this.serverPort + "/game/" + hostName).method(HttpMethod.DELETE).param("playerName",playerName).send();
			client.stop();
		} catch (Exception e) {
			logger.error("Something went wrong when sending the request to delete an user.");
			return;
		}
		if(response.getStatus() != 200){//400 or 403
			logger.error("Something went wrong when deleting an user.");
		}
		
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
