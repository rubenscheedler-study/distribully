package distribully.model.rules;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class RevertTurnOrderRule extends Rule {

	public RevertTurnOrderRule(DistribullyModel model) {
		super(model);
	}

	@Override
	public TurnState execute() {
		TurnState turnState = new TurnState(model.getTurnState().getNextPlayer(),model.getTurnState().getToPick(),model.getTurnState().getDirection(),model.getTurnState().getAction(),false,"");
		
		turnState.setDirection(turnState.getDirection()*-1);
		turnState.setNextPlayer(rotateTurn(turnState.getDirection()));
		turnState.setAction(model.getTurnState().getNextPlayer() + " reverted the playing order.");
		return turnState;
	}

	@Override
	public String toString() {
		return "Revert order";
	}

}
