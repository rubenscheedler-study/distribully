package distribully.controller;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import spark.servlet.SparkApplication;

public class SetUpHandler implements SparkApplication {
	Gson gson = new Gson();
	public  void init() {
		post("/invite/:name", "application/json" , (request, response) -> handleInvite(request, response), gson::toJson);
		get("/invite/:name", "application/json" , (request, response) -> handleInvite(request, response), gson::toJson); //testing feature

		put("/startRules", "application/json", (request, response) -> startRulesSelect(request, response), gson::toJson);
		put("/accept/:name", "application/json", (request, response) -> handleAccept(request, response), gson::toJson);
		put("/reject", "application/json", (request, response) -> handleReject(request, response), gson::toJson);
		
		delete("/leave", "application/json", (request, response) -> handleGameLeave(request, response), gson::toJson);

		post("/users/", "application/json", (request, response) -> setPlayerList(request, response), gson::toJson);
		post("/rules/:playerId", "application/json", (request, response) -> setRulesForPlayer(request, response), gson::toJson);
	}

	private String handleInvite(spark.Request request, spark.Response response){

		//Show popup met ja/nee
		//Kies naam
		//Stuur bericht terug
		System.out.println(":hostName has invited.");
		response.status(204);
		return "Received invite";
		
		//Is in game?
	}
	private String handleAccept(spark.Request request, spark.Response response){

		//Zet vinkje oid
		//Add to player list
		//Zet naam erbij
		System.out.println(":name has Accepted.");
		response.status(200);
		return "accepted";
	}
	private String handleReject(spark.Request request, spark.Response response){

		//Zet kruisje oid 
		System.out.println(":ip has Denied.");
		response.status(200);
		return "rejected";
	}
	
	private String handleGameLeave(spark.Request request, spark.Response response){

		//Remove from game/ playerlist 
		//Different behavior als rules select of tijdens game
		//Hou stack -> hoe dan met rules? (This is a broadcast)
		System.out.println(":name has left.");
		response.status(200);
		return "Left the game";
	}

	private String setPlayerList(spark.Request request, spark.Response response){
		//This.playerlist = payload
		System.out.println("Players have been set.");
		response.status(200);
		return "These are the players";
	}

	private String startRulesSelect(spark.Request request, spark.Response response){
		//Open Rules Screen
		System.out.println("Start rules select.");
		response.status(200);
		return "You may select a rule";
	}
	private String setRulesForPlayer(spark.Request request, spark.Response response){
		System.out.println("Rules from :id.");
		//Add rules to player in list (This is a broadcast)
		//If all rules are set, start
		response.status(200);
		return "Start the game";
	}

}
