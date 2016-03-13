package server;

import static spark.Spark.*;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
		gson = new Gson();
		parser = new JsonParser();
		get("/players", "application/json" , (request, response) -> getUserList(request, response)); //testing feature
//
//		put("/available", "application/json", (request, response) -> setAvailable(request, response), gson::toJson);
//		put("/busy", "application/json", (request, response) -> setUnavailable(request, response), gson::toJson);
//		
//		delete("/leave", "application/json", (request, response) -> handleGameLeave(request, response), gson::toJson);
//
		get("/players/:name", "application/json", (request, response) -> addToPlayerList(request, response));
	}

	private String handleInvite(Request request, Response response){

		//Show popup met ja/nee
		//Kies naam
		//Stuur bericht terug
		System.out.println(":hostName has invited.");
		response.status(204);
		return "Received invite";
		
		//Is in game?
	}
	
	private JsonObject addToPlayerList(Request request, Response response){
		String playerName = request.params(":name");
		JsonObject returnObject = new JsonObject();
		if(players.stream().anyMatch(x -> x.getName() == playerName)){
			response.status(403);
			returnObject.addProperty("Message", "Username already exists");
			return returnObject;
		}
		int port;
		try{
			port = Integer.valueOf(request.queryParams("port"));
		} catch (NumberFormatException e){
			//Id was unable to be cast to a number
			logger.warning("port is NaN.");
			response.status(400);
			return responseAnswer.createMessageResponse("error", "port is not a valid number.");
		}

		System.out.println("User joined");
		System.out.println(request.ip() +"---" +  request.port());
		response.status(201);
		returnObject.addProperty("Message", "User has been added");
		return returnObject;
	}
	
	private JsonArray getUserList(Request request, Response response){
		System.out.println("Userlist requested");
		response.status(200);
		return parser.parse(gson.toJson(players)).getAsJsonArray();
	}
}
