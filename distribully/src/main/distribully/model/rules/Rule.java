package distribully.model.rules;

import distribully.model.DistribullyModel;
import distribully.model.TurnState;

public abstract class Rule {
	protected DistribullyModel model;
	public Rule(DistribullyModel model) {
		this.model = model;
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
	
	/**
	 * Reads the model turnState. Gets the current player's index, increases his index by rotateCount and returns the next player.
	 * @param rotateCount
	 * @return
	 */
	public String rotateTurn(int rotateCount) {
		//increase 
		int numPlayers = model.getGamePlayerList().getPlayers().size();
		int index = model.getGamePlayerList().getPlayers().indexOf(model.getGamePlayerList().getPlayerByNickname(model.getTurnState().getNextPlayer()));
		String nextPlayer = model.getGamePlayerList().getPlayers().get((index + numPlayers + rotateCount) % numPlayers ).getName(); //Ensure we stay in the range
		return nextPlayer;
	}
}
