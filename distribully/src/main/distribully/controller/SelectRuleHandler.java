package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JComboBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import distribully.model.DistribullyModel;
import distribully.model.rules.EmptyRule;
import distribully.model.rules.Rule;

public class SelectRuleHandler implements ActionListener {

	private int cardNumber;
	private DistribullyModel model;
	private JComboBox<Rule> dropdown;
	private static Logger logger;
	
	public SelectRuleHandler(int cardNumber, JComboBox<Rule> dropdown, DistribullyModel model) {
		logger = LoggerFactory.getLogger("controller.SelectRuleHandler");
		this.cardNumber = cardNumber;
		this.model = model;
		this.dropdown = dropdown;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Rule rule  = (Rule)dropdown.getSelectedItem();
		logger.info("ruleselected:  n:" + cardNumber + "|rule: " + rule.toString());
		if (!rule.toString().equals( (new EmptyRule(null)).toString())) { //Whether the card has a special rule now
			model.setCardRule(cardNumber, rule);
		} else {
			model.removeCardRule(cardNumber);
		}
	}

}
