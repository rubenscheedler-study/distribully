package distribully.model;

import java.util.ArrayList;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethod;

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
	
	public void deleteFromServer(String playerName){
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
