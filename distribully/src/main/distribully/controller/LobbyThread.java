package distribully.controller;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import distribully.model.DistribullyModel;
import distribully.model.Player;

public class LobbyThread extends Thread {
	private volatile boolean inLobby = false;
	ServerSocket serverSocket;
	DistribullyModel model;
	String hostName;
	
	public LobbyThread(DistribullyModel model, String hostName) {
		this.model = model;
		this.hostName = hostName;
		inLobby = true;
		this.start();
	}
	public void run() {
		System.out.println("Starting lobby...");
		while (inLobby) {
			model.getGamePlayerList().fillWithGamePlayers(hostName);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Lobbythread interrupted during sleep!");
				e.printStackTrace();
			}			
		}
		System.out.println("No longer in lobby.");
	}
	public void setInLobby(boolean inLobby){
		this.inLobby = inLobby;
	}
	
}
