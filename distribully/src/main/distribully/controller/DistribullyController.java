package distribully.controller;
import distribully.view.*;
import static spark.Spark.*;

import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class DistribullyController {
	public static void main(String[] args) {
		new DistribullyWindow();
		try {
		    System.out.println("starting Jetty...");

		    Server server = new Server(8080);
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
		    server.join();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Started!");

		
	}

}
