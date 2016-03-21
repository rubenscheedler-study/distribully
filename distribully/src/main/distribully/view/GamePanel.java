package distribully.view;

import java.awt.Dimension;

import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;

public class GamePanel extends DistribullyPanel implements IObserver {

	private static final long serialVersionUID = -7619956785679933010L;

	private DistribullyModel model;
	private Dimension size;
	private HandPanel handPanel;
	private StacksPanel stacksPanel;
	
	public GamePanel(DistribullyModel model, Dimension size) {
		this.model = model;
		this.size = size;
		this.handPanel = new HandPanel(model,size);
		this.stacksPanel = new StacksPanel(model,size);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		render();
	}
	
	public void render() {
		this.removeAll();
		this.add(handPanel);
		this.add(stacksPanel);
		this.revalidate();
		this.repaint();
	}

	private DistribullyPanel renderStacks() {
		DistribullyPanel stacksPanel = new DistribullyPanel();
		
		return stacksPanel;
	}

	private DistribullyPanel renderHand() {
		DistribullyPanel handPanel = new DistribullyPanel();
		
		return handPanel;
	}

	@Override
	public void update(IObservable observable, Object changedObject) {
		// TODO Auto-generated method stub
		this.render();
	}
}
