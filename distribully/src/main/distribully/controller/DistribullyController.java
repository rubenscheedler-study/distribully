package distribully.controller;
import distribully.view.*;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public static GameState GAME_STATE;

public class DistribullyController {
	public static void main(String[] args) {
		new DistribullyWindow();
		try {
		    System.out.println("starting Jetty...");

		    Server server = new Server(4567); //set port here
		    WebAppContext webAppContext = new WebAppContext();
		    webAppContext.setContextPath("/");

		    /* Important: Use getResource */
		    String webxmlLocation = DistribullyController.class.getResource("/webapp/WEB-INF/web.xml").toString();
		    webAppContext.setDescriptor(webxmlLocation);

		    /* Important: Use getResource */
		    String resLocation = DistribullyController.class.getResource("/webapp").toString();
		    webAppContext.setResourceBase(resLocation);

		    webAppContext.setParentLoaderPriority(true);

		    server.setHandler(webAppContext);

		    server.start();
		    System.out.println("Server has started.");
		    server.join();
		    
		    
			
		} catch (Exception e) {
			System.out.println("Something went wrong while starting the server.");
			e.printStackTrace();
			System.exit(0);
		}
		//This is never reached
		
		HttpClient client = new HttpClient();
	    try {
			client.start();
			client.newRequest("http://localhost:8080/invite/x").method(HttpMethod.GET).send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	}
}
