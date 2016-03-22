package distribully.model;

import javax.swing.JOptionPane;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends ConnectingComponent implements Comparable<Player> {
	private String name;
	private String ip;
	private int port;
	private boolean available;
	private transient boolean readyToPlay;
	private static Logger logger;
	
	public Player(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		available = true;
		logger = LoggerFactory.getLogger("model.Player");
		setReadyToPlay(false);
	}
	
	public Player(String serverAddress, int serverPort, String name, String ip, int port) {
		super(serverAddress, serverPort);
		this.name = name;
		this.ip = ip;
		this.port = port;
		available = true;
		setReadyToPlay(false);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) { //Update your availability
		this.available = available;
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		try {
			client.start();
			response = client.newRequest(this.serverAddress + ":" + this.serverPort + "/players/" + this.name)
					.method(HttpMethod.PUT)
					.param("available", available+"")
					.send();
			client.stop();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
				    "Something went wrong updating your availability.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			logger.error("Something went wrong updating your availability.");
		}
		if(response.getStatus() != 200){ //400 or 403
			logger.error("Setting availability failed.");
		}
	}
	
	public void deleteFromServer(){ //Remove yourself from the server
		HttpClient client = new HttpClient();
		ContentResponse response;
		try {
			client.start();
			response = client.newRequest(this.serverAddress + ":" + this.serverPort + "/players/" + this.name).method(HttpMethod.DELETE).send();
			client.stop();
		} catch (Exception e) {
			logger.error("Something went wrong while sending the request for deleting yourself");
			return;
		}
		if(response.getStatus() != 201){
			logger.error("Something went wrong on the server while deleting yourself.");
		}
	}

	public boolean isReadyToPlay() {
		return readyToPlay;
	}

	public void setReadyToPlay(boolean readyToPlay) {
		this.readyToPlay = readyToPlay;
	}

	@Override
	public int compareTo(Player o) {
		//name is unique identifier, so only compare those
		return this.name.compareTo(o.getName());
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (available != other.available)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}
