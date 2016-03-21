package distribully.model.rules;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class DrawFiveRule extends Rule {

	public DrawFiveRule(DistribullyModel model) {
		super(model);
	}

	@Override
	public TurnState execute() {
		TurnState turnState = new TurnState(model.getTurnState().getNextPlayer(),model.getTurnState().getToPick(),model.getTurnState().getDirection(),model.getTurnState().getAction());


		
		turnState.setNextPlayer(rotateTurn(turnState.getDirection()));
		turnState.setAction("drawing 5");
		turnState.setToPick(turnState.getToPick()+5);
		return turnState;
	}

	@Override
	public String toString() {
		return "Draw five";
	}

}
