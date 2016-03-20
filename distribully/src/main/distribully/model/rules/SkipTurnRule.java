package distribully.model.rules;

import distribully.model.Stack;

public class SkipTurnRule extends Rule {

	public SkipTurnRule(Stack stack) {
		super(stack);
	}

	@Override
	public void execute() {

	}

	@Override
	public String toString() {
		return "skip turn";
	}

}
