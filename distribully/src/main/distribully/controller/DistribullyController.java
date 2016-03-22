package distribully.controller;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import distribully.model.DistribullyModel;
import distribully.view.*;

public class DistribullyController { //Main class
	private static DistribullyWindow view;
	private DistribullyModel model;
	private static Logger logger;
	//Threads are static for easy start/stop
	public static WaitForInviteThread waitForInviteThread = null;
	public static LobbyThread lobbyThread = null;
	public static UpdateGameHostThread updateGameHostThread = null;
	public static GameConsumerThread consumerThread = null;
	public static ArrayList<InviteThread> InviteThreadList = new ArrayList<InviteThread>(); 
	
	public DistribullyController() {
		logger = LoggerFactory.getLogger("controller.DistribullyController");
		logger.info("Started application");
		model = new DistribullyModel();
		model.setGAME_STATE(GameState.NOT_PLAYING);
		view = new DistribullyWindow(model);
		new AskPortHandler(model);
		new AskUsernameHandler(model);
	}

	//Tell the game to quit
	public static void sendCloseEvent(){
		view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
	}

	public static void main(String[] args) {
		new DistribullyController();
	}
}
