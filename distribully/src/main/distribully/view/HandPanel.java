package distribully.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import distribully.model.Card;
import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;
import distribully.model.Player;

public class HandPanel extends DistribullyPanel implements IObserver {

	private static final long serialVersionUID = -7619956785679933010L;
	private final int LEFT_OFFSET = 40;
	private final int TOP_OFFSET = 25;
	private final int RIGHT_MARGIN = 40;
	private int IMAGE_WIDTH;
	private int IMAGE_HEIGHT;
	private int CARD_VISIBLE_WIDTH;
	private DistribullyModel model;
	private Dimension size;
	
	public HandPanel(DistribullyModel model, Dimension size) {
		this.model = model;
		this.size = size;
		IMAGE_HEIGHT = (int)((size.height/2)*0.8);
		IMAGE_WIDTH = (int) (500.0 * ((double)IMAGE_HEIGHT/726.0));
		CARD_VISIBLE_WIDTH = (int)((double)IMAGE_WIDTH/5.75);
		calculateAndSetSize();
	}
	
	public void calculateAndSetSize() {
		//width: 
		int panelWidth = LEFT_OFFSET + ((model.getHand().size()-1)*CARD_VISIBLE_WIDTH) + IMAGE_WIDTH + RIGHT_MARGIN;
		System.out.println(panelWidth);
		this.setMinimumSize(new Dimension(panelWidth, this.size.height));
		this.setPreferredSize(new Dimension(panelWidth, this.size.height));
		this.setMaximumSize(new Dimension(panelWidth, this.size.height));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		calculateAndSetSize();
		
		
		//1) draw the header "Your Hand"
		g.setFont(this.headerFont);
		g.setColor(new Color(230,230,230));
		g.drawString("Your Hand", LEFT_OFFSET, TOP_OFFSET);
		
		//2) draw the hand of the player
		int i = 0;
		for (Card c : model.getHand()) {
			String imageName = c.getImage();
			//System.out.println(System.getProperty("user.dir") + "|||" + imageName);
			File image = new File(imageName);
			BufferedImage img = null;
			
			try {
				img = ImageIO.read(image);
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			
			g.drawImage(img,LEFT_OFFSET+CARD_VISIBLE_WIDTH*i,TOP_OFFSET+15,IMAGE_WIDTH,IMAGE_HEIGHT,null);
			i++;
		}
		//3 draw header "Current Stack of Players"
		g.drawString("Current Stacks of Players:",LEFT_OFFSET,TOP_OFFSET+30+IMAGE_HEIGHT);
		
		int j = 0;
		//4) draw top of stacks
		for (Player player : model.getOnlinePlayerList().getPlayers()) {
			g.drawString(player.getName(),LEFT_OFFSET+j*(IMAGE_WIDTH+15), TOP_OFFSET+30+IMAGE_HEIGHT+30);
			File image = new File(Card.getARandomCard().getImage());
			BufferedImage img = null;
			
			try {
				img = ImageIO.read(image);
			} catch(IOException e) {
				e.printStackTrace();
			}
		    g.drawImage(img,LEFT_OFFSET+j*(IMAGE_WIDTH+15),TOP_OFFSET+30+IMAGE_HEIGHT+30,IMAGE_WIDTH,IMAGE_HEIGHT,null);
			j++;
		}
	}

	@Override
	public void update(IObservable observable, Object changedObject) {
		this.repaint();
	}
}
