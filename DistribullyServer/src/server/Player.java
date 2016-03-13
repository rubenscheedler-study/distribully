package server;

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
	
	public void setAvailable(boolean available){
		this.available = available;
	}
	
	public String getAdress(){
		return ip+ ":" + port;
	}
	
	public String getName(){
		return name;
	}
	
}