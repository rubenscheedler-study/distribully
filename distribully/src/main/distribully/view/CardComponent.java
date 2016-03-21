package distribully.view;

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
	
	public CardComponent(int posX, int posY, int width, int height, Card card) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.card = card;
		this.clicked = false;
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
		
		
		g.drawImage(img,posX,posY,width,height,null);
	}
	
	/**
	 * returns if a given click position matches a place within this image.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean wasClicked(int x, int y) {
		return x >= posX && x < (posX + width) && y >= posY && y < (posY + height);
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	
	public void click() {
		this.clicked = !this.clicked;
	}
}
