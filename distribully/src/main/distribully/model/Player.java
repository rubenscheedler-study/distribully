package distribully.model;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Player extends ConnectingComponent {

	private String name;
	private String ip;
	private int port;
	private boolean available;
	
	public Player(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		available = true;
	}
	
	public Player(String serverAddress, int serverPort, String name, String ip, int port) {
		super(serverAddress, serverPort);
		this.name = name;
		this.ip = ip;
		this.port = port;
		available = true;
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
	
	public void setAvailable(boolean available) {
		this.available = available;
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		try {
			client.start();
			response = client.newRequest(this.serverAddress + ":" + this.serverPort + "/players/" + this.name)
					.method(HttpMethod.POST)
					.param("available", available+"")
					.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
