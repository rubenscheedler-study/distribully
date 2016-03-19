package distribully.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ClientList extends ConnectingComponent implements IObservable {
	ArrayList<Player> players;
	//list of observers
	ArrayList<IObserver> observers = new ArrayList<IObserver>();
	
	public ClientList(String serverAddress, int serverPort) {
		super(serverAddress,serverPort);
		players = new ArrayList<Player>();
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	public void deleteFromServer(String playerName){//TODO moet naar player
		HttpClient client = new HttpClient();
		try {
			client.start();
			client.newRequest(this.serverAddress + ":" + this.serverPort + "/players/" + playerName).method(HttpMethod.DELETE).send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//TODO: Handle response?
	}

	
	/**
	 * Fills the list with the game players on the server of the game owned by hostName
	 * @param hostName
	 */
	public void fillWithGamePlayers(String hostName) {
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
		
		if (response.getStatus() == 200) {
			JsonParser jsonParser = new JsonParser();
			System.out.println(response.getContentAsString());
			JsonElement je = jsonParser.parse(response.getContentAsString());
			JsonArray ja = je.getAsJsonArray();
			
			Gson gson = new Gson();
			Player[] players = gson.fromJson(ja, Player[].class);
			ArrayList<Player> playerList  = new ArrayList<Player>(Arrays.asList(players));
			this.players.removeAll(this.players);
			this.players.addAll(playerList);
			this.notifyObservers();

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
		this.players.removeAll(players);
		playerList.forEach(player -> this.players.add(player));
		System.out.println("updated list of players:" + players.size() + "," + observers.size());
		this.notifyObservers();
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
		this.observers.forEach(observer -> observer.update(this));
	}
	
}
