package server;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;
import spark.Response;

public class PlayerServerController {
	Gson gson;
	HashMap<String, ArrayList<Player>> players;
	JsonParser parser;

	public PlayerServerController(){
		this.init();
	}

	private void init() {
		players = new HashMap<String, ArrayList<Player>>();
		gson = new Gson();
		parser = new JsonParser();
		get("/game/:name", "application/json" , (request, response) -> getPlayerList(request, response)); 

		delete("/game/:name", "application/json", (request, response) -> removeFromPlayerList(request, response));
		delete("/endGame/:name", "application/json", (request, response) -> deletePlayerList(request, response));

		post("/game/:name", "application/json", (request, response) -> addToPlayerList(request, response));
		post("/createGame/:name", "application/json", (request, response) -> createPlayerList(request, response));
	}

	private JsonElement getPlayerList(Request request, Response response) {
		String gameName = request.params(":name");
		JsonObject returnObject = new JsonObject();
		if(gameName == null || gameName.trim().equals("")){
			response.status(400);
			returnObject.addProperty("Message", "Game name is missing.");
			return returnObject;
		}
		//System.out.println("Playerlist requested");
		if(players.containsKey(gameName)){
			response.status(200);
			return parser.parse(gson.toJson(players.get(gameName))).getAsJsonArray();
		}else{
			response.status(403);
			returnObject.addProperty("Message", "Game does not exist.");
			return returnObject;
		}
	}

	private JsonObject addToPlayerList(Request request, Response response){
		String gameName = request.params(":name");
		JsonObject returnObject = new JsonObject();
		if(gameName == null || gameName.trim().equals("")){
			response.status(400);
			returnObject.addProperty("Message", "Game name is missing.");
			return returnObject;
		}
		
		if(!players.containsKey(gameName)){
			response.status(403);
			returnObject.addProperty("Message", "Game does not exist");
			return returnObject;
		}
		
		String playerJson = request.queryParams("player");
		JsonElement je = parser.parse(playerJson);
		JsonObject playerObject = je.getAsJsonObject();
		Player player = gson.fromJson(playerObject, Player.class);

		players.get(gameName).add(player);
		response.status(201);
		System.out.println("User " + player.getName()+ " added to game: " + gameName);
		returnObject.addProperty("Message", player.getName() + " has been added");
		return returnObject;
	}

	private JsonObject createPlayerList(Request request, Response response){
		String gameName = request.params(":name");
		JsonObject returnObject = new JsonObject();
		if(gameName == null || gameName.trim().equals("")){
			response.status(400);
			returnObject.addProperty("Message", "Game name is missing.");
			return returnObject;
		}
		String playerJson = request.queryParams("player");
		JsonElement je = parser.parse(playerJson);
		JsonObject playerObject = je.getAsJsonObject();
		Player player = gson.fromJson(playerObject, Player.class);

		ArrayList<Player> list = new ArrayList<Player>();
		list.add(player);
		players.put(gameName, list);
		System.out.println("Game created");
		response.status(201);
		return returnObject;
	}

	private JsonObject removeFromPlayerList(Request request, Response response){
		String gameName = request.params(":name");
		JsonObject returnObject = new JsonObject();
		if(gameName == null || gameName.trim().equals("")){
			response.status(400);
			returnObject.addProperty("Message", "Game name is missing.");
			return returnObject;
		}
		String playerString = request.queryParams("playerName");
		if(!players.containsKey(gameName)){
			response.status(403);
			returnObject.addProperty("Message", "Game does not exist.");
			return returnObject;
		}

		if(players.get(gameName).stream().anyMatch(x -> x.getName().equals(playerString))){
			players.get(gameName).removeIf(p -> p.getName().equals(playerString));
		}else{
			response.status(403);
			returnObject.addProperty("Message", "PlayerName is not in list.");
			return returnObject;
		}

		if(players.get(gameName).size() == 0){
			players.remove(gameName);
			response.status(200);
			returnObject.addProperty("Message", "Game is now empty and therefore removed.");
			return returnObject;
		}
		System.out.println("User " + playerString+ " removed from game: " + gameName);
		response.status(200);
		returnObject.addProperty("Message", "Player "+ playerString +" was removed.");
		return returnObject;
	}

	private JsonObject deletePlayerList(Request request, Response response){
		String gameName = request.params(":name");
		JsonObject returnObject = new JsonObject();
		if(gameName == null || gameName.trim().equals("")){
			response.status(400);
			returnObject.addProperty("Message", "Game name is missing.");
			return returnObject;
		}
		players.remove(gameName);
		response.status(200);
		returnObject.addProperty("Message", "Game is now gone.");
		return returnObject;
	}
}
