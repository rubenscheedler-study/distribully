package distribully.model;

public class TurnState {
	private String nextPlayer;
	private int toPick;
	private int direction;
	String action;
	
	public TurnState(String nextPlayer, int toPick, int direction, String action){
		this.nextPlayer = nextPlayer;
		this.toPick = toPick;
		this.direction = direction;
		this.action = action;
	}

	public String getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(String nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	public int getToPick() {
		return toPick;
	}

	public void setToPick(int toPick) {
		this.toPick = toPick;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
