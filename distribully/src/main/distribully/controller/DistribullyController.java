package distribully.controller;
import distribully.model.ClientList;
import distribully.view.*;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class DistribullyController {
	public static GameState GAME_STATE;
	DistribullyWindow view;
	ClientList clientList;
	
	public DistribullyController() {
		view = new DistribullyWindow();
		clientList = new ClientList();
		view.setClientList(clientList);
	}
	
	public static void main(String[] args) {
		new DistribullyController();
	}
}
