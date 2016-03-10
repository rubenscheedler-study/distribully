package distribully.controller;

import static spark.Spark.*;

import spark.servlet.SparkApplication;

public class SetUpHandler implements SparkApplication {

	public  void init() {


		get("/invite/:name" , (request, response) -> handleInvite(request, response)); //get??

		put("/startRules", (request, response) -> startRulesSelect(request, response));
		put("/accept/:name", (request, response) -> handleAccept(request, response));
		put("/reject", (request, response) -> handleReject(request, response));
		
		delete("/leave", (request, response) -> handleGameLeave(request, response));

		post("/users/", (request, response) -> setPlayerList(request, response));
		post("/rules/:playerId", (request, response) -> setRulesForPlayer(request, response));
	}

	private String handleInvite(spark.Request request, spark.Response response){

		//Show popup met ja/nee
		//Kies naam
		//Stuur bericht terug
		System.out.println(":hostName has invited.");
		response.status(200);
		return "Received invite";
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
