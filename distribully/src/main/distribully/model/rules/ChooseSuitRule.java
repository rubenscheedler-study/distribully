package distribully.model.rules;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public class ChooseSuitRule extends Rule {

	public ChooseSuitRule(DistribullyModel model) {
		super(model);
	}

	@Override
	public TurnState execute() {
		return new TurnState(model.getTurnState().getNextPlayer(),
				model.getTurnState().getToPick(),
				model.getTurnState().getDirection(),
				model.getTurnState().getNextPlayer() + " played 'choose suit'.");
	}

	@Override
	public String toString() {
		return "Choose suit";
	}
	

}
