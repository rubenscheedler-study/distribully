package distribully.view;

import javax.swing.JButton;

import distribully.controller.InviteUserHandler;
import distribully.model.DistribullyModel;

public class InviteButton extends JButton {

	private static final long serialVersionUID = 2980466415141228958L;
	
	private DistribullyModel model;
	private String nickname;
	
	public InviteButton(DistribullyModel model, String forNickname) {
		this.model = model;
		this.nickname = forNickname;
		this.setText("Invite");
		this.addActionListener(new InviteUserHandler(nickname,this.model));
	}
}
