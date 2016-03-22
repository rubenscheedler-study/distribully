package distribully.view;

import java.awt.Color;
import java.awt.Graphics;

public class DrawCardsComponent extends DrawableComponent {

	public DrawCardsComponent(int posX, int posY, int width, int height) {
		super(posX, posY, width, height);
	}

	@Override
	public void draw(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(posX, posY, width, height);
		g.setColor(Color.RED);
		g.drawString("Draw cards",posX+10,posY+15);
		g.setColor(color);
	}

}
