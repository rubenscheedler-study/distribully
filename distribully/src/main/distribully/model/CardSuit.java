package distribully.model;

public enum CardSuit {
	HEARTS (0),
	DIAMONDS (1),
	CLUBS (2),
	SPADES (3);
		
	private int v;
	
	CardSuit(int val) {
		v = val;
	}
	
	public int getV() { //Get int associated with the suit
		return this.v;
	}
}
