package distribully.model;

public class TurnState {
	String nextPlayer;
	int toPick;
	int direction;
	
	public TurnState(String nextPlayer, int toPick, int direction){
		this.nextPlayer = nextPlayer;
		this.toPick = toPick;
		this.direction = direction;
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
	
}
