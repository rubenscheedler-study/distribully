package server;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;

public class ServerController {
	public static void main(String[] args) {
		init();
	}
	
	static Gson gson = new Gson();
	private static void init() {
//		get("/users", "application/json" , (request, response) -> getUserList(request, response), gson::toJson); //testing feature
//
//		put("/available", "application/json", (request, response) -> setAvailable(request, response), gson::toJson);
//		put("/busy", "application/json", (request, response) -> setUnavailable(request, response), gson::toJson);
//		
//		delete("/leave", "application/json", (request, response) -> handleGameLeave(request, response), gson::toJson);
//
//		post("/join/:name", "application/json", (request, response) -> setPlayerList(request, response), gson::toJson);
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
}
