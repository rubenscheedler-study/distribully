package distribully.view;

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

public class HandPanel extends DistribullyPanel implements IObserver {

	private static final long serialVersionUID = -7619956785679933010L;

	private DistribullyModel model;
	private Dimension size;
	
	public HandPanel(DistribullyModel model, Dimension size) {
		this.model = model;
		this.size = size;
		this.setMinimumSize(new Dimension(this.size.width, this.size.height/2));
		this.setPreferredSize(new Dimension(this.size.width, this.size.height/2));
		this.setMaximumSize(new Dimension(this.size.width, this.size.height/2));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//size of a rendered card:
		int imageWidth = this.size.width/model.getHand().size();
		int imageHeight = imageWidth * (726/500);//regular size of image
		
		//image is too high => resize to maximum allowed height
		if (imageHeight > (this.size.height-30)) {
			double shrinkFactor = imageHeight/(this.size.height-30);
			imageWidth *= shrinkFactor;
			imageHeight *= shrinkFactor;
		}
		int i = 0;
		for (Card c : model.getHand()) {
			String imageName = "src/main/distribully/cards/" + c.getImage();
			//System.out.println(System.getProperty("user.dir") + "|||" + imageName);
			File image = new File(imageName);
			BufferedImage img = null;
			
			try {
				img = ImageIO.read(image);
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			
			g.drawImage(img,imageWidth*i,0,imageWidth,imageHeight,null);
			i++;
		}
	}

	@Override
	public void update(IObservable observable, Object changedObject) {
		this.repaint();
	}
}
