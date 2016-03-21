package distribully.controller;



public enum GameState {
	NOT_PLAYING (0),
	INVITING_USERS (1),
	IN_LOBBY (2),
	SETTING_RULES (3),
	WAITING_FOR_GAMESTART (4),
	IN_GAME (5);
	
	private int v;

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	GameState(int v) {
		this.v = v;
	}


}
