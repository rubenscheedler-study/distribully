package distribully.controller;


import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.view.DistribullyWindow;

public class JoinClientListHandler {
	
	DistribullyWindow frame;
	
	public JoinClientListHandler(DistribullyWindow frame) {
		this.frame = frame;
		String chosenNickname = "";

		do {
			chosenNickname = askUserForName();
			
			if (chosenNickname == null) { //user does not want to pick a username => close application
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				return;
			}
			chosenNickname = chosenNickname.trim();
			
			if (chosenNickname.contains(" ") || !nicknameAvailable(chosenNickname)) {
				chosenNickname = "";
			}
			
		}
		while (chosenNickname.equals(""));
		
		//check if name is unique
		System.out.println("setting model nickname:" + chosenNickname);
		frame.getModel().setNickname(chosenNickname);
		//this update is required to fetch the current user from the server as well:
		new ClientListUpdateHandler(frame.getModel());
		DistribullyController.waitForInviteThread = new WaitForInviteThread(frame.getModel());
	}

	
	public String askUserForName() {
		return JOptionPane.showInputDialog(frame, "Please enter your nickname");
	}
	
	public boolean nicknameAvailable(String nickname) {
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		try {
			client.start();
			String sa = frame.getModel().getServerAddress();
			response = client.newRequest(sa + ":" + frame.getModel().getServerPort() + "/players/" + nickname)
					.method(HttpMethod.POST)
					.param("port", frame.getModel().getMyPort()+"")
					.send();
			client.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (response.getStatus() == 201) {
			JsonParser jsonParser = new JsonParser();
			System.out.println(response.getContentAsString());
			JsonElement jsonElement = jsonParser.parse(response.getContentAsString());
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			String myAddress = jsonObject.get("ip").getAsString();
			frame.getModel().setMyIP(myAddress);
			return true;
		} else {
			return false;
		}
	}
}
