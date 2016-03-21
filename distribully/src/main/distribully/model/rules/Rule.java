package distribully.model.rules;

import distribully.model.Stack;
import distribully.model.TurnState;

public abstract class Rule {
	private Stack stack;
	
	public Rule(Stack stack) {
		this.stack = stack;
	}
	/**
	 * applies the effect of this card on the stack and/or the owner of the stack.
	 * @return 
	 */
	public abstract TurnState execute();
	/**
	 * 
	 * @return The description/name of the rule.
	 */
	public abstract String toString();
}
