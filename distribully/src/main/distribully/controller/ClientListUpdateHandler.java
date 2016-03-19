package distribully.controller;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	public ClientListUpdateHandler(DistribullyModel model) {
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		try {
			client.start();
			response = client.newRequest(model.getServerAddress() + ":" + model.getServerPort() + "/players").method(HttpMethod.GET).send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsonParser jsonParser = new JsonParser();
		System.out.println(response.getContentAsString());
		JsonElement je = jsonParser.parse(response.getContentAsString());
		JsonArray ja = je.getAsJsonArray();
		
		Gson gson = new Gson();
		Player[] players = gson.fromJson(ja, Player[].class);
		ArrayList<Player> playerList  = new ArrayList<Player>(Arrays.asList(players));
		
		playerList.forEach(player -> player.setServerAddress(model.getServerAddress()));
		playerList.forEach(player -> player.setServerPort(model.getServerPort()));
		
		model.getClientList().setPlayers(playerList);
	}
}
