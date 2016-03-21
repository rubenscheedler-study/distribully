package distribully.model.rules;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class DrawTwoRule extends Rule {

	public DrawTwoRule(DistribullyModel model) {
		super(model);
	}

	@Override
	public TurnState execute() {
		TurnState turnState = new TurnState(model.getTurnState().getNextPlayer(),model.getTurnState().getToPick(),model.getTurnState().getDirection(),model.getTurnState().getAction());
		
		turnState.setNextPlayer(rotateTurn(turnState.getDirection()));
		turnState.setAction("draw 2");
		turnState.setToPick(turnState.getToPick()+2);
		return turnState;
	}

	@Override
	public String toString() {
		return "Draw two";
	}

}
