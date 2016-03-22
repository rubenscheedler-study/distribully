package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JComboBox;

import distribully.model.DistribullyModel;
import distribully.model.rules.EmptyRule;
import distribully.model.rules.Rule;

public class SelectRuleHandler implements ActionListener {

	private int cardNumber;
	private DistribullyModel model;
	private JComboBox<Rule> dropdown;
	private static Logger logger;
	
	public SelectRuleHandler(int cardNumber, JComboBox<Rule> dropdown, DistribullyModel model) {
		logger = Logger.getLogger("controller.SelectRuleHandler");
		logger.setParent(Logger.getLogger("controller.DistribullyController"));
		this.cardNumber = cardNumber;
		this.model = model;
		this.dropdown = dropdown;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Rule rule  = (Rule)dropdown.getSelectedItem();
		logger.fine("ruleselected:  n:" + cardNumber + "|rule: " + rule.toString());
		if (!rule.toString().equals( (new EmptyRule(null)).toString())) {
			model.setCardRule(cardNumber, rule);
		} else {
			model.removeCardRule(cardNumber);
		}
	}

}
