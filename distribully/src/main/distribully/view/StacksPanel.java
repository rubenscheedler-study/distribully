package distribully.view;

import java.awt.Dimension;

import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;

public class StacksPanel extends DistribullyPanel implements IObserver {
	
	private static final long serialVersionUID = -7619956785679933010L;

	private DistribullyModel model;
	private Dimension size;
	
	public StacksPanel(DistribullyModel model, Dimension size) {
		this.model = model;
		this.size = size;
	}
	
	@Override
	public void update(IObservable observable) {
		// TODO Auto-generated method stub
		
	}
}
