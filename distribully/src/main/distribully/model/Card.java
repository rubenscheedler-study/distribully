package distribully.model;

public class Card {
	private int number;//ace=11,jack=12,queen=13,king=14
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
	
	public String getImage() {
		String img = "";
		if (number > 10) {//map to the right textual name
			switch (number) {
			case 11:
				img += "ace";
				break;
			case 12:
				img += "jack2";
				break;
			case 13:
				img += "queen2";
				break;
			case 14:
				img += "king2";
				break;
			default:
				img += number;//easier to detect the problem than empty string
				break;
			}

		} else {
			img += number;
		}
		img += "_of_";
		//add the suit to the image file name
		
		switch (this.suit) {
		case HEARTS:
			img += "hearts";
			break;
		case SPADES:
			img += "spades";
			break;
		case CLUBS:
			img += "clubs";
			break;
		case DIAMONDS:
			img += "diamonds";
			break;
		default:
			img += "";
			break;
		}
		
		//add the file extension
		return img + ".png";
	}
}
