package distribully.model;

import java.util.ArrayList;
import java.util.Observable;

public class ClientList extends Observable {
	ArrayList<Player> players;
	
	public ClientList() {
		players = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	/**
	 * adds p to the list of players and notifies the observing view.
	 * @param p Player to add
	 */
	public void addPlayer(Player p) {
		this.players.add(p);
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * removes p from the list of players and notifies the observing view.
	 * @param p Player object to remove
	 */
	public void removePlayer(Player p) {
		this.players.remove(p);
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * overwrites the current player list with the received playerList
	 * @param playerList
	 */
	public void setPlayers(ArrayList<Player> playerList) {
		this.players.removeAll(players);
		playerList.forEach(player -> this.players.add(player));
		System.out.println("updated list of players:" + playerList.size() + "," + this.countObservers());
		this.setChanged();
		this.notifyObservers();
	}
	
}
