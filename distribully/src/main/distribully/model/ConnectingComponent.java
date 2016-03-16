package distribully.model;

public abstract class ConnectingComponent {
	protected String serverAddress;
	protected int serverPort;
	
	public ConnectingComponent() {}
	
	public ConnectingComponent(String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	
}
