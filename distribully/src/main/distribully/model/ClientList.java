package distribully.model;

import java.util.ArrayList;
import java.util.Observable;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

public class ClientList extends Observable {
	ArrayList<Player> players;
	
	public ClientList() {
		players = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	public void deleteFromServer(String playerName){
		HttpClient client = new HttpClient();
		try {
			client.start();
			client.newRequest("http://82.72.30.166:4567" + "/players/" + playerName).method(HttpMethod.DELETE).send(); //TODO: fix hardcode
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * overwrites the current player list with the received playerList
	 * @param playerList
	 */
	public void setPlayers(ArrayList<Player> playerList) {
		//this.players.removeAll(players);
		playerList.forEach(player -> this.players.add(player));
		System.out.println("updated list of players:" + players.size() + "," + this.countObservers());
		this.setChanged();
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
	
}
