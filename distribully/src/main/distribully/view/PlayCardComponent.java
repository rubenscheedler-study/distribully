package distribully.view;

import java.awt.Color;
import java.awt.Graphics;

public class PlayCardComponent extends DrawableComponent {

	public PlayCardComponent(int posX, int posY, int width, int height) {
		super(posX, posY, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {
		Color color = g.getColor();
		//Font font = g.getFont();
		g.setColor(Color.GRAY);
		g.fillRect(posX, posY, width, height);
		g.setColor(Color.BLACK);
		g.drawString("Play",posX+10,posY+15);
		g.setColor(color);
		//g.setFont(font);
	}

}
