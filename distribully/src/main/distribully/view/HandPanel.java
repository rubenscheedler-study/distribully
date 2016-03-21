package distribully.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

	private ArrayList<CardComponent> handCards;
	private ArrayList<CardComponent> stackCards;
	private CardComponent selectedCard;
	private CardComponent selectedStackCard;

	public HandPanel(DistribullyModel model, Dimension size) {
		this.model = model;
		this.size = size;
		IMAGE_HEIGHT = (int)((size.height/2)*0.8);
		IMAGE_WIDTH = (int) (500.0 * ((double)IMAGE_HEIGHT/726.0));
		CARD_VISIBLE_WIDTH = (int)((double)IMAGE_WIDTH/5.75);
		this.model.addObserver(this);
		calculateAndSetSize();
		handCards = new ArrayList<CardComponent>();
		stackCards = new ArrayList<CardComponent>();
		this.addMouseListener(new CardClickListener());
	}

	public void calculateAndSetSize() {
		//width: 
		int panelWidth = LEFT_OFFSET + ((model.getHand().size()-1)*CARD_VISIBLE_WIDTH) + IMAGE_WIDTH + RIGHT_MARGIN;
		this.setMinimumSize(new Dimension(panelWidth, this.size.height));
		this.setPreferredSize(new Dimension(panelWidth, this.size.height));
		this.setMaximumSize(new Dimension(panelWidth, this.size.height));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		calculateAndSetSize();
		System.out.println("repaint handpanel");

		//1) draw the header "Your Hand"
		g.setFont(this.headerFont);
		g.setColor(new Color(230,230,230));
		g.drawString("Your Hand", LEFT_OFFSET, TOP_OFFSET);

		//2) draw the hand of the player

		int i = 0;
		boolean refreshHandComponents = this.handCards.size() != model.getHand().size();
		if (refreshHandComponents) {
			this.handCards = new ArrayList<CardComponent>();
		}
		for (Card c : model.getHand()) {
			CardComponent cardComponent;
			if (refreshHandComponents) {
				int ind = model.getHand().indexOf(c);
				System.out.println("refresh!" + ind);

				int visibleWidth = ind == (model.getHand().size()-1) ? IMAGE_WIDTH : CARD_VISIBLE_WIDTH;
				if (visibleWidth == IMAGE_WIDTH) {
					System.out.println("full range!");
				}
				cardComponent = new CardComponent(LEFT_OFFSET+CARD_VISIBLE_WIDTH*i,TOP_OFFSET+15,IMAGE_WIDTH,IMAGE_HEIGHT,c,visibleWidth);
				this.handCards.add(cardComponent);
			} else {
				cardComponent = this.handCards.get(i);
			}
			cardComponent.draw(g, selectedCard != null && selectedCard.equals(cardComponent));
			i++;
		}

		//3 draw header "Current Stack of Players"
		g.drawString("Current Stacks of Players:",LEFT_OFFSET,TOP_OFFSET+30+IMAGE_HEIGHT);

		boolean refreshStackComponents = true || this.stackCards.size() != model.getOnlinePlayerList().getPlayers().size();
		this.stackCards = new ArrayList<CardComponent>();
		int j = 0;
		//4) draw top of stacks
		for (Player player : model.getGamePlayerList().getPlayers()) {
			g.drawString(player.getName(),LEFT_OFFSET+j*(IMAGE_WIDTH+15), TOP_OFFSET+30+IMAGE_HEIGHT+30);
			CardComponent cardComponent;
			if (refreshStackComponents) {
				Card topCard = null;
				if (model.getTopOfStacks().containsKey(player)) {
					topCard = model.getTopOfStacks().get(player);
				}
				cardComponent = new CardComponent(LEFT_OFFSET+j*(IMAGE_WIDTH+15),TOP_OFFSET+30+IMAGE_HEIGHT+40,IMAGE_WIDTH,IMAGE_HEIGHT,topCard,IMAGE_WIDTH);
				this.stackCards.add(cardComponent);
			} else {
				cardComponent = this.stackCards.get(j);
			}
			cardComponent.draw(g, selectedStackCard != null && selectedStackCard.equals(cardComponent));
			j++;
		}
	}

	@Override
	public void update(IObservable observable, Object changedObject) {
		System.out.println("observer update");
		this.repaint();
	}

	class CardClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (model.getTurnState().getNextPlayer().equals(model.getMe().getName())) {
				for (CardComponent component : handCards) {

					if (component.wasClicked(e.getX(), e.getY())) {
						if (selectedCard != null && selectedCard.equals(component)) {//unselected if already selected
							selectedCard = null;
						} else {
							selectedCard = component;
						}
						break;
					}
				}

				for (CardComponent component : stackCards) {//TODO validate
					if (component.wasClicked(e.getX(), e.getY())) {
						if (selectedStackCard != null && selectedStackCard.equals(component)) {//unselected if already selected
							selectedStackCard = null;
						} else {
							selectedStackCard = component;
						}
						break;
					}
				}

				//TODO if both are set, GO GO GO
				if (selectedStackCard != null && selectedCard != null) {
					System.out.println("GO GO GO");
				}
				revalidate();
				repaint();
			}
		}
	}

}
