package distribully.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import distribully.model.Card;

public class CardComponent {
	private int posX;
	private int posY;
	private int width;
	private int height;
	private Card card;
	private boolean clicked;
	private int actualWidth;
	
	public CardComponent(int posX, int posY, int width, int height, Card card, int actualWidth) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.card = card;
		this.clicked = false;
		this.actualWidth = actualWidth;
	}
	
	public void draw(Graphics g) {
		String imageName = card.getImage();
		//System.out.println(System.getProperty("user.dir") + "|||" + imageName);
		File image = new File(imageName);
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(image);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if (clicked) {
			System.out.println("clicked in com");
			g.setColor(Color.GREEN);
			g.fillRoundRect(posX-5, posY-5, width+10, height+10, 10, 10);
			g.setColor(Color.WHITE);
		}
		g.drawImage(img,posX,posY,width,height,null);
	}
	
	/**
	 * returns if a given click position matches a place within this image.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean wasClicked(int x, int y) {
		return x >= posX && x < (posX + actualWidth) && y >= posY && y < (posY + height);
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	
	public void click() {
		System.out.println("click()");
		this.clicked = !this.clicked;
	}
	
	public Card getCard() {
		return this.card;
	}
	
	public String toString() {
		return "[posX:" + posX + ",posY:" + posY + ",width:" + width + ",height:" + height + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + actualWidth;
		result = prime * result + ((card == null) ? 0 : card.hashCode());
		result = prime * result + (clicked ? 1231 : 1237);
		result = prime * result + height;
		result = prime * result + posX;
		result = prime * result + posY;
		result = prime * result + width;
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
		CardComponent other = (CardComponent) obj;
		if (actualWidth != other.actualWidth)
			return false;
		if (card == null) {
			if (other.card != null)
				return false;
		} else if (!card.equals(other.card))
			return false;
		if (clicked != other.clicked)
			return false;
		if (height != other.height)
			return false;
		if (posX != other.posX)
			return false;
		if (posY != other.posY)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
}
