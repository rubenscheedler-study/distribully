package distribully.model.rules;

import distribully.model.DistribullyModel;
import distribully.model.Stack;
import distribully.model.TurnState;

public class EmptyRule extends Rule {

	public EmptyRule(DistribullyModel model) {
		super(model);
	}

	@Override
	public TurnState execute() {
		return null;
	}

	@Override
	public String toString() {
		return "No rule";
	}

}
