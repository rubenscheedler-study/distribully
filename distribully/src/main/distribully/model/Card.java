package distribully.model;

import java.util.ArrayList;
import java.util.Random;

public class Card {
	private int number;//ace=11,jack=12,queen=13,king=14
	private CardSuit suit;
	private long uniqueMaker;
	
	public Card(int number, CardSuit suit) {
		this.setNumber(number);
		this.setSuit(suit);
		
		Random randomno = new Random();
		long value = randomno.nextLong();
		uniqueMaker = value;
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
		if (number > 10) {//map to the right textual name
			switch (number) {
			case 11:
				name += "ace";
				break;
			case 12:
				name += "jack";
				break;
			case 13:
				name += "queen";
				break;
			case 14:
				name += "king";
				break;
			default:
				name += number;//easier to detect the problem than empty string
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
	
	public ArrayList<Card> getFullDeck() {
		ArrayList<Card> deck = new ArrayList<Card>();
		
		for (int i = 2; i < 15; i++) {
			deck.add(new Card(i,CardSuit.CLUBS));
		}
		for (int i = 2; i < 15; i++) {
			deck.add(new Card(i,CardSuit.DIAMONDS));
		}
		for (int i = 2; i < 15; i++) {
			deck.add(new Card(i,CardSuit.HEARTS));
		}
		for (int i = 2; i < 15; i++) {
			deck.add(new Card(i,CardSuit.SPADES));
		}
		
		return deck;
	}
	
	public static Card getARandomCard() {
		int cardIndex = (int)(2.0 + (Math.random()*12.0));
		int cardSuitIndex = (int)(Math.random()*3.0);
		CardSuit suit;
		switch (cardSuitIndex) {
		case 0:
			suit = CardSuit.CLUBS;
			break;
		case 1:
			suit = CardSuit.DIAMONDS;
			break;
		case 2:
			suit = CardSuit.HEARTS;
			break;
		case 3:
		default:
			suit = CardSuit.SPADES;
			break;
		}
		return new Card(cardIndex,suit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		result = prime * result + (int) (uniqueMaker ^ (uniqueMaker >>> 32));
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
		if (uniqueMaker != other.uniqueMaker)
			return false;
		return true;
	}

	public boolean isSameCard(Card other) {
		return other.getNumber() == this.number && other.getSuit() == this.suit;
	}
}
