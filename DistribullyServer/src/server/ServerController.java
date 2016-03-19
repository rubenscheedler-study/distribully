package server;

import static spark.Spark.*;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import server.Player;
import spark.Request;
import spark.Response;


public class ServerController {
	
	Gson gson;
	ArrayList<Player> players;
	JsonParser parser;
	
	public static void main(String[] args) {
		(new ServerController()).init();
	}
	
	private void init() {
		players = new ArrayList<Player>();
		Player x = new Player("Lekker_in_verrote bananen_knijpen", "Meneer", 666);
		x.setAvailable(false);
		players.add(x);
		players.add(new Player("Zag_je_me_vliegen", "kartoffel", 111));
		players.add(new Player("Buiten_om_je_stad", "omringeld", 8888));
		gson = new Gson();
		parser = new JsonParser();
		get("/players", "application/json" , (request, response) -> getUserList(request, response)); //testing feature

		put("/players/:name", "application/json", (request, response) -> setAvailable(request, response));
		
		delete("/players/:name", "application/json", (request, response) -> removeFromPlayerList(request, response));

		post("/players/:name", "application/json", (request, response) -> addToPlayerList(request, response));
	}
	
	private JsonObject addToPlayerList(Request request, Response response){
		String playerName = request.params(":name");
		JsonObject returnObject = new JsonObject();
		if(players.stream().anyMatch(x -> x.getName().equals(playerName))){
			response.status(403);
			returnObject.addProperty("Message", "Username already exists");
			System.out.println("Username "+ playerName + " was already taken.");
			return returnObject;
		}
		
		int port;
		try{
			port = Integer.valueOf(request.queryParams("port"));
		} catch (NumberFormatException e){
			//Port was unable to be cast to a number
			System.out.println("Port is NaN.");
			response.status(400);
			returnObject.addProperty("Message", "Port is not a valid number.");
			return returnObject;
		}

		System.out.println(playerName + " joined");
		Player newPlayer = new Player(playerName, request.ip(), port);
		players.add(newPlayer);
		response.status(201);
		returnObject.addProperty("Message", playerName + " has been added");
		returnObject.addProperty("ip", request.ip());
		return returnObject;
	}
	
	private JsonObject setAvailable(Request request, Response response){
		String playerName = request.params(":name");
		System.out.println(playerName + " is updating his availability to: " + request.queryParams("available"));
		JsonObject returnObject = new JsonObject();
		if(!players.stream().anyMatch(x -> x.getName().equals(playerName))){
			response.status(403);
			returnObject.addProperty("Message", "Username is not in the playerlist");
			return returnObject;
		}
		
		boolean available;
		if(request.queryParams("available") == null){
			System.out.println("Available is NaB.");
			response.status(400);
			returnObject.addProperty("Message", "Available is not provided.");
			return returnObject;
		}else if(request.queryParams("available").equals("true")){ //Only this exact format is allowed
			available = true;
		}else if(request.queryParams("available").equals("false")){ //Only this exact format is allowed
			available = false;
		}else{
			System.out.println("Available is wrong format.");
			response.status(400);
			returnObject.addProperty("Message", "Available is not in the right format (true/false).");
			return returnObject;
		}
		
		System.out.println(playerName + "'s availability is " + available);
		players.stream().filter(x -> x.getName().equals(playerName)).findFirst().get().setAvailable(available);
		response.status(200);
		returnObject.addProperty("Message", playerName + "'s availability is "+ available);
		return returnObject;
	}
	
	private JsonObject removeFromPlayerList(Request request, Response response){
		String playerName = request.params(":name");
		JsonObject returnObject = new JsonObject();
		if(!players.stream().anyMatch(x -> x.getName().equals(playerName))){
			response.status(403);
			returnObject.addProperty("Message", "Username is not in the playerlist");
			return returnObject;
		}

		System.out.println(playerName + " left");
		Player toDelete = players.stream().filter(x -> x.getName().equals(playerName)).findFirst().get();
		players.remove(toDelete);
		response.status(201);
		returnObject.addProperty("Message", playerName + " has been removed");
		return returnObject;
	}
	
	private JsonArray getUserList(Request request, Response response){
		System.out.println("Userlist requested");
		response.status(200);
		return parser.parse(gson.toJson(players)).getAsJsonArray();
	}
}
