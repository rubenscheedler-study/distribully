package distribully.model.rules;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class PlayAgainRule extends Rule {

	public PlayAgainRule(DistribullyModel model) {
		super(model);
	}

	@Override
	public TurnState execute() {
		TurnState turnState = new TurnState(model.getTurnState().getNextPlayer(),model.getTurnState().getToPick(),model.getTurnState().getDirection(),model.getTurnState().getAction(),false,"");
		
		turnState.setNextPlayer(rotateTurn(0));
		turnState.setAction(model.getTurnState().getNextPlayer() + " played 'play again'.");
		return turnState;
	}

	@Override
	public String toString() {
		return "Play again";
	}

}
