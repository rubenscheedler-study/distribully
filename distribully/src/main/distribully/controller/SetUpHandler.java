package distribully.controller;

import static spark.Spark.*;

import spark.servlet.SparkApplication;

public class SetUpHandler implements SparkApplication {

	public  void init() {


		get("/invite" , (request, response) -> handleInvite(request, response));

		put("/startRules", (request, response) -> startRulesSelect(request, response));
		put("/accept/:name", (request, response) -> handleAccept(request, response));
		put("/reject", (request, response) -> handleReject(request, response));
		
		delete("/leave", (request, response) -> handleGameLeave(request, response));

		post("/users/", (request, response) -> setPlayerList(request, response));
		post("/rules", (request, response) -> setRulesForPlayer(request, response));
	}

	private String handleInvite(spark.Request request, spark.Response response){

		//Show popup met ja/nee
		//Kies naam
		//Stuur bericht terug

		response.status(200);
		return "Received invite";
	}
	private String handleAccept(spark.Request request, spark.Response response){

		//Zet vinkje oid
		//Add to player list
		//Zet naam erbij

		response.status(200);
		return "accepted";
	}
	private String handleReject(spark.Request request, spark.Response response){

		//Zet kruisje oid 
		
		response.status(200);
		return "rejected";
	}
	
	private String handleGameLeave(spark.Request request, spark.Response response){

		//Remove from game/ playerlist 
		//Different behavior als rules select of tijdens game
		//Hou stack -> hoe dan met rules? (This is a broadcast)
		
		response.status(200);
		return "Left the game";
	}

	private String setPlayerList(spark.Request request, spark.Response response){
		//This.playerlist = payload
		response.status(200);
		return "These are the players";
	}

	private String startRulesSelect(spark.Request request, spark.Response response){
		//Open Rules Screen
		response.status(200);
		return "You may select a rule";
	}
	private String setRulesForPlayer(spark.Request request, spark.Response response){
		//Add rules to player in list (This is a broadcast)
		//If all rules are set, start
		response.status(200);
		return "Start the game";
	}

}
