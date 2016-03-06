package distribully.model;

public enum CardSuit {
	
	HEARTS (2),
	DIAMONDS (3),
	CLUBS (4),
	SPADES (5);
		
	private int v;
	
	CardSuit(int val) {
		v = val;
	}
	
	public static boolean areSameColor(CardSuit first, CardSuit second) {
		return ((first.getV() & 2) == (second.getV() & 2)) //return true if both cards are red or both cards are black
			|| ((first.getV() & 4) == (second.getV() & 4));
	}
	
	public int getV() {
		return this.v;
	}
}
