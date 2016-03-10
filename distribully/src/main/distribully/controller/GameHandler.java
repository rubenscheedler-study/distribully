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

		System.out.println("Received card: cardID");
		//get card ID
		//get stackId
		//If stack = yours:
		//Handle card
		System.out.println("The action with this card is: action");
		System.out.println("The next player is: PlayerId");
		//end if
		
		//Update view

		//return new player or broadcast new player?
		
		response.status(200);
		
		return "card";
	}
	
	private String handleTurn(spark.Request request, spark.Response response){

		//if playerId = you:
		System.out.println("I am the next player.");
		//Play turn
		//Else:
		//Update view

		response.status(200);
		return "I shall play";
	}
	
	
	private String handleCompletion(spark.Request request, spark.Response response){

		//get winner
		//Display x (name by id) has won
		//Return to main screen
		System.out.println(":PlayerName has won.");

		response.status(200);
		return "WIN";
	}
	
}
