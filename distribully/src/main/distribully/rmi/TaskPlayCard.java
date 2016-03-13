package distribully.rmi;

import java.io.Serializable;

import distribully.model.Card;
import distribully.model.Stack;

public class TaskPlayCard implements Task<Boolean>, Serializable {

	private static final long serialVersionUID = 3201747976549469225L;
	private Card cardBeingPlayed;
	private Stack stack;
	
	@Override
	public Boolean execute() {
		System.out.println("invite task!");
		stack.apply(cardBeingPlayed);
		return true;
	}

}
