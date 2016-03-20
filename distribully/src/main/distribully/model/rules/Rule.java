package distribully.model.rules;

import distribully.model.Stack;

public abstract class Rule {
	private Stack stack;
	
	public Rule(Stack stack) {
		this.stack = stack;
	}
	/**
	 * applies the effect of this card on the stack and/or the owner of the stack.
	 */
	public abstract void execute();
	/**
	 * 
	 * @return The description/name of the rule.
	 */
	public abstract String toString();
}
