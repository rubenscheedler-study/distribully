package distribully.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import distribully.model.DistribullyModel;
import distribully.model.Player;

public class ClientListUpdateHandler {
	
	private static Logger logger;
	public ClientListUpdateHandler(DistribullyModel model) {
		logger = Logger.getLogger("controller.ClientListUpdateHandler");
		logger.setParent(Logger.getLogger("controller.DistribullyController"));
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		try {
			client.start();
			response = client.newRequest(model.getServerAddress() + ":" + model.getServerPort() + "/players").method(HttpMethod.GET).send();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
				    "The server is currently offline. \n"
					+ "Start the server first",
				    "Server error",
				    JOptionPane.WARNING_MESSAGE);
			System.exit(69);  //Service unavailable
		}
	
	
		//TODO: handle response
		
		JsonParser jsonParser = new JsonParser();
		logger.fine(response.getContentAsString());
		JsonElement je = jsonParser.parse(response.getContentAsString());
		JsonArray ja = je.getAsJsonArray();
		
		Gson gson = new Gson();
		Player[] players = gson.fromJson(ja, Player[].class);
		ArrayList<Player> playerList  = new ArrayList<Player>(Arrays.asList(players));
		
		playerList.forEach(player -> player.setServerAddress(model.getServerAddress()));
		playerList.forEach(player -> player.setServerPort(model.getServerPort()));
		
		model.getOnlinePlayerList().setPlayers(playerList);
	}
}
