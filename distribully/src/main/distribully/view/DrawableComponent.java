package distribully.view;

import java.awt.Graphics;

public abstract class DrawableComponent {
	protected int posX;
	protected int posY;
	protected int width;
	protected int height;
	
	public DrawableComponent(int posX, int posY, int width, int height) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
	}
	
	public abstract void draw(Graphics g);
	
	public boolean wasClicked(int x, int y) {
		return x >= posX && x < (posX + width) && y >= posY && y < (posY + height);
	}
}
