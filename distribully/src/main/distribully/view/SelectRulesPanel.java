package distribully.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;

import distribully.controller.SelectRuleHandler;
import distribully.model.Card;
import distribully.model.CardSuit;
import distribully.model.DistribullyModel;
import distribully.model.IObservable;
import distribully.model.IObserver;
import distribully.model.rules.EmptyRule;
import distribully.model.rules.Rule;

public class SelectRulesPanel extends DistribullyPanel implements IObserver {

	private static final long serialVersionUID = -7807476804456336016L;
	
	private DistribullyWindow view;
	private DistribullyModel model;
	private Dimension size;
	
	public SelectRulesPanel(DistribullyWindow window, Dimension size) {
		this.view = window;
		this.model = window.getModel();
		this.model.addObserver(this);
		this.size = size;
		this.render();
	}
	
	public void render() {
		this.removeAll();
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		
		this.add(getHeaderPanel());
		
		ArrayList<Rule> availableRules = new ArrayList<Rule>();
		
		availableRules.add(new EmptyRule(null));//generates top option "no rule" in dropdown
		
		//filter out all the rules that are already assigned
		availableRules.addAll(model.getAllRules().stream()
								   .filter(r -> !model.getChoosenRules().containsValue(r))
								   .collect(Collectors.toCollection(ArrayList<Rule>::new)));
		System.out.println(availableRules.size());
		//add a panel for each card that allows the user to select a rule for that card
		for (int i = 2; i < 15; i++) {
			this.add(renderSelectForCard(i,availableRules));
		}
		
		//add a finish button that notifies the host that all rules have been chosen
		this.add(new FinishSelectRulesButton(model));
		this.revalidate();
		this.repaint();
	}
	
	public DistribullyPanel getHeaderPanel() {
		DistribullyPanel headerPanel = new DistribullyPanel();
		headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setMinimumSize(new Dimension(this.size.width,40));
		headerPanel.setPreferredSize(new Dimension(this.size.width,40));
		headerPanel.setMaximumSize(new Dimension(this.size.width,40));
		DistribullyTextLabel header = new DistribullyTextLabel("Pick the rules for each card, that will apply to your stack.");
		header.setHeaderFont();
		headerPanel.add(header);
		return headerPanel;
	}
	
	/**
	 * render a panel row with on the left the card name and on the right a dropdown to associate a rule.
	 * @param cardNumber Integer index of the card [2,14]
	 * @param availableRules Set of not picked rules, plus the empty rule
	 * @return
	 */
	public DistribullyPanel renderSelectForCard(int cardNumber, ArrayList<Rule> rules) {
		ArrayList<Rule> availableRules = new ArrayList<Rule>();
		availableRules.addAll(rules);
		
		DistribullyPanel rulePanel = new DistribullyPanel();
		rulePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		String cardName = new Card(cardNumber, CardSuit.HEARTS).getCardName();
		
		DistribullyTextLabel cardNameLabel = new DistribullyTextLabel(cardName + ":");
		cardNameLabel.setMinimumSize(new Dimension(400, 25));
		cardNameLabel.setPreferredSize(new Dimension(400, 25));
		rulePanel.add(cardNameLabel);
		
		//check if card has an associated rule, add it to the dropdown
		if (model.getChoosenRules().containsKey(cardNumber)) {
			availableRules.add(model.getChoosenRules().get(cardNumber));
		}
		
		Rule[] availableRuleArray = new Rule[availableRules.size()];
		JComboBox<Rule> ruleDropdown = new JComboBox<Rule>(availableRules.toArray(availableRuleArray));
		
		//check if card has an associated rule, select it in the dropdown
		if (model.getChoosenRules().containsKey(cardNumber)) {
			ruleDropdown.setSelectedItem((model.getChoosenRules().get(cardNumber)));
		}

		ruleDropdown.addActionListener(new SelectRuleHandler(cardNumber,ruleDropdown,model));
		
		rulePanel.add(ruleDropdown);

		return rulePanel;
	}

	@Override
	public void update(IObservable observable, Object changedObject) {
		this.render();
	}
}
