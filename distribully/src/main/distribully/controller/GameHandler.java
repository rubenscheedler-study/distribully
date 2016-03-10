package distribully.controller;

import static spark.Spark.*;

import spark.servlet.SparkApplication;

public class GameHandler implements SparkApplication {

	public  void init() {
		put("/card/:stackId/:cardId", (request, response) -> handleCard(request, response));
		put("/nextTurn/:playerId", (request, response) -> handleTurn(request, response));
		delete("/finishedGame", (request, response) -> handleCompletion(request, response));
	}
	
	private String handleCard(spark.Request request, spark.Response response){

		//get card ID
		//get stackId
		//If stack = yours:
		//Handle card
		//end if
		
		//Update view

		//return new player or broadcast new player?
		
		response.status(200);
		return "card";
	}
	
	private String handleTurn(spark.Request request, spark.Response response){

		//if playerId = you:
		//Play turn
		//Else:
		//Update view

		response.status(200);
		return "WIN";
	}
	
	
	private String handleCompletion(spark.Request request, spark.Response response){

		//get winner
		//Display x has won
		//Return to main screen

		response.status(200);
		return "WIN";
	}
	
}
