package distribully.controller;

public enum GameState {
	NOT_PLAYING (0),
	INVITING_USERS (1),
	SETTING_RULES (2),
	IN_GAME (3);
	
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
