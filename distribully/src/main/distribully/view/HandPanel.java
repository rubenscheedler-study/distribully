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
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.Card;
import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;
import distribully.model.Player;
import distribully.model.TurnState;

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
	
	private PlayCardComponent playCardComponent;
	private DrawCardsComponent drawCardsComponent;

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
		if (model.isMyTurn()) {
			playCardComponent = new PlayCardComponent(LEFT_OFFSET+200-10, TOP_OFFSET-15, 100, 20);
			drawCardsComponent = new DrawCardsComponent(LEFT_OFFSET+400-10, TOP_OFFSET-15, 150, 20);
			playCardComponent.draw(g);
			drawCardsComponent.draw(g);
		}
		
		//render action
		if (model.getTurnState() != null) {
			String actionString = model.getTurnState().getAction();
			g.drawString(actionString,LEFT_OFFSET+600,TOP_OFFSET);
		}	
		
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
			Color oldColor = g.getColor();
			if (model.getNextPlayer().equals(player.getName())) {
				g.setColor(Color.GREEN);
			}
			g.drawString(player.getName(),LEFT_OFFSET+j*(IMAGE_WIDTH+15), TOP_OFFSET+30+IMAGE_HEIGHT+30);
			g.setColor(oldColor);
			
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
			if (model.isMyTurn()) {
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

				if (playCardComponent.wasClicked(e.getX(), e.getY()))  {
					if (selectedStackCard != null && selectedCard != null) {
						if (selectedStackCard.getCard().getNumber() == selectedCard.getCard().getNumber()
							|| selectedStackCard.getCard().getSuit() == selectedCard.getCard().getSuit()) {
							
							//remove card from hand
							model.getHand().remove(selectedCard.getCard());//TODO notify?
							
							ConnectionFactory factory = new ConnectionFactory();
							factory.setHost(model.getMe().getIp());
							Connection connection;
							try {
								connection = factory.newConnection();

								Channel channel = connection.createChannel();

								channel.exchangeDeclare(model.getNickname(), "fanout");
								JsonObject message = new JsonObject();
								message.addProperty("cardId",  selectedCard.getCard().getNumber());
								message.addProperty("suitId", selectedCard.getCard().getSuit().getV());
								String ownerName = "";
								for (Player owner : model.getTopOfStacks().keySet()) {
									if (model.getTopOfStacks().get(owner).equals(selectedStackCard.getCard())) {
										ownerName = owner.getName();
									}
								}
								message.addProperty("stackOwner", ownerName);
								channel.basicPublish(model.getNickname(), "PlayCard", null, message.toString().getBytes());
								System.out.println(" [x] Sent '" + message + "'");

								channel.close();
								connection.close();
							} catch (IOException | TimeoutException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						} else {
							JOptionPane.showMessageDialog(null,
							          "The card you selected may not be played on that stack.",
							          "Invalid play",
							          JOptionPane.WARNING_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null,
						          "Please choose a card to play and a stack to play it on.",
						          "Incomplete Selection",
						          JOptionPane.WARNING_MESSAGE);
					}
				}
				
				if (drawCardsComponent.wasClicked(e.getX(),e.getY())) {
					TurnState toSend = model.getTurnState();
					int toDraw = model.getTurnState().getToPick()+1;
					toSend.setNextPlayer(model.getNextPlayer());
					toSend.setAction(model.getNickname() + " has drawn "+ toDraw+" cards.");
					toSend.setToPick(0);
					model.draw(toDraw, toSend);
				}

				revalidate();
				repaint();
			}
		}
	}

}
