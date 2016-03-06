package distribully.model;

import java.util.ArrayList;
import java.util.Observable;

public class Console extends Observable {
	
	private ArrayList<String> messages;
	
	public Console() {
		messages = new ArrayList<String>();
		messages.add("mandje");
		messages.add(" van");
		messages.add(" tiggelaar");
	}
	
	public void addMessage(String message) {
		messages.add(message);
		this.notifyObservers();
	}
	
	public ArrayList<String> getMessages() {
		return this.messages;
	}
}
