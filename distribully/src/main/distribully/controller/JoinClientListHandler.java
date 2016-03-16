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
		String choosenNickname = "";

		do {
			choosenNickname = askUserForName();
			
			if (choosenNickname == null) { //user does not want to pick a username => close application
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				return;
			}
			
			if (choosenNickname.contains(" ") || !nicknameAvailable(choosenNickname)) {
				choosenNickname = "";
			}
			
		}
		while (choosenNickname.equals(""));
		
		//check if name is unique
		System.out.println("setting model nickname:" + choosenNickname);
		frame.getModel().setNickname(choosenNickname);
		DistribullyController.StartWaitForInvite();
	}

	
	public String askUserForName() {
		return JOptionPane.showInputDialog(frame, "Please enter your nickname");
	}
	
	public boolean nicknameAvailable(String nickname) {
		HttpClient client = new HttpClient();
		ContentResponse response = null;
		try {
			client.start();
			response = client.newRequest(frame.getModel().getServerAddress() + ":" + frame.getModel().getServerPort() + "/players/" + nickname)
					.method(HttpMethod.POST)
					.param("port", frame.getModel().getMyPort()+"")
					.send();
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
