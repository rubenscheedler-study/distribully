package distribully.model;

import java.util.Random;

public class Card {
	private int number;//ace=11,jack=12,queen=13,king=14
	private CardSuit suit;
	private long uniqueIdentifier;
	
	public Card(int number, CardSuit suit) {
		this.setNumber(number);
		this.setSuit(suit);
		
		Random randomNum = new Random();
		long value = randomNum.nextLong();
		//Since in this game the user can have multiples of the same card, we must generate an unique identifier to differentiate
		uniqueIdentifier = value; 
	}
	public Card(int number) {
		this.setNumber(number);
		
		Random randomNum = new Random();
		long value = randomNum.nextLong();
		//Since in this game the user can have multiples of the same card, we must generate an unique identifier to differentiate
		uniqueIdentifier = value; 
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
	
	public String getCardName() {
		String name = "";
		if (number > 10) {//Map to the right textual name
			switch (number) {
			case 11:
				name += "Ace";
				break;
			case 12:
				name += "Jack";
				break;
			case 13:
				name += "Queen";
				break;
			case 14:
				name += "King";
				break;
			default:
				name += number;//Should never be hit
				break;
			}

		} else {
			name = number+"";
		}
		return name;
	}
	public String getImage() {
		String img = "src/main/distribully/cards/" + this.getCardName();
		
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
	
	public static Card getARandomCard() {
		int cardIndex = (int)(2.0 + (Math.random()*12.0));
		int cardSuitIndex = (int)(Math.random()*3.0);
		CardSuit suit = CardSuit.values()[cardSuitIndex];
		return new Card(cardIndex,suit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		result = prime * result + (int) (uniqueIdentifier ^ (uniqueIdentifier >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (number != other.number)
			return false;
		if (suit != other.suit)
			return false;
		if (uniqueIdentifier != other.uniqueIdentifier)
			return false;
		return true;
	}
}
