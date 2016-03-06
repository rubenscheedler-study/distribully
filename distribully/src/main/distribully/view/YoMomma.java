package distribully.view;

import static spark.Spark.*;

import javax.servlet.http.HttpServlet;

import spark.Route;
import spark.Spark;
import spark.servlet.SparkApplication;

public class YoMomma implements SparkApplication {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2201449631605696139L;

	
	
	
	public  void init() {
		
		get("/", (request, response) -> {
				response.redirect("/hello");
				return null;
		});
		
		get("/hello" , (request, response) -> "Hello World!");

		get("/hello/:name", (request, response) -> {
				return  String.format("Hello, %s!", request.params(":name"));
		});
	}

}

