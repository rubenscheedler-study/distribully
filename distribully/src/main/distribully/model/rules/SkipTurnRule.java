package distribully.model.rules;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class SkipTurnRule extends Rule {

	public SkipTurnRule(DistribullyModel model) {
		super(model);
	}

	@Override
	public TurnState execute() {
		TurnState turnState = new TurnState(model.getTurnState().getNextPlayer(),model.getTurnState().getToPick(),model.getTurnState().getDirection(),model.getTurnState().getAction());

		turnState.setNextPlayer(rotateTurn(turnState.getDirection()*2));
		turnState.setAction("skip a player");
		return turnState;
	}

	@Override
	public String toString() {
		return "Skip turn";
	}

}
