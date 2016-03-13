package distribully.model;

public class Player {

	private String name;
	private String ip;
	private int port;
	private boolean available;
	
	public Player(String name, String ip, int port){
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
	}
}
