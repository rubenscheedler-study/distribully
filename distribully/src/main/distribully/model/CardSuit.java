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
	
	public static boolean areSameColor(CardSuit first, CardSuit second) {
		return ((first.getV() & 2) == (second.getV() & 2)) //return true if both cards are red or both cards are black
			|| ((first.getV() & 4) == (second.getV() & 4));
	}
	
	public int getV() {
		return this.v;
	}
	
	CardSuit getSuit(int suit) {
		switch (suit) {
		case 0:
			return this.HEARTS;
		case 1:
			return this.DIAMONDS;
		case 2:
			return this.CLUBS;
		case 3:
			return this.SPADES;
		default:
			return null;
		}
	}
}
