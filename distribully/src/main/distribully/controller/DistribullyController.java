package distribully.controller;
import distribully.model.DistribullyModel;
import distribully.view.*;

public class DistribullyController {
	
	DistribullyWindow view;
	DistribullyModel model;
	
	public DistribullyController() {
		model = new DistribullyModel();
		view = new DistribullyWindow(model);
		
	}
	
	public static void main(String[] args) {
		new DistribullyController();
	}
	
	public void updatePlayerList() {
		
	}
}
