package distribully.controller;


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
		String choosenNickName = "";
		boolean nickNameIsUnique = false;
		do {
			choosenNickName = askUserForName();
			tryToClaimNickName(choosenNickName);
			
		}
		while (choosenNickName.equals(""));
		//check if name is unique
		frame.getModel().setNickname(choosenNickName);
	}

	
	public String askUserForName() {
		return JOptionPane.showInputDialog(frame, "Please enter your nickname");
	}
	
	public boolean tryToClaimNickName(String nickname) {
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
		
		JsonParser jsonParser = new JsonParser();
		System.out.println(response.getContentAsString());
		JsonElement jsonElement = jsonParser.parse(response.getContentAsString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String myAddress = jsonObject.get("ip").getAsString();
		return true;
	}
}
