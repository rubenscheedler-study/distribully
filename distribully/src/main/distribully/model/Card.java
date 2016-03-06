package distribully.model;

public class Card {
	private int number;
	private CardSuit suit;
	
	public Card(int number, CardSuit suit) {
		this.setNumber(number);
		this.setSuit(suit);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public CardSuit getSuit() {
		return suit;
	}

	public void setSuit(CardSuit suit) {
		this.suit = suit;
	}
	
	
}
