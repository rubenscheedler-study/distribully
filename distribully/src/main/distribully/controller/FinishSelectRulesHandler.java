package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.DistribullyModel;

public class FinishSelectRulesHandler  implements ActionListener {

	private DistribullyModel model;
	private Logger logger;
	public FinishSelectRulesHandler(DistribullyModel model) {
		this.model = model;	
		logger = Logger.getLogger("controller.FinishRulesHandler");
		logger.setParent(Logger.getLogger("controller.DistribullyController"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(model.getChoosenRules().size() == model.getAllRules().size()){
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(model.getMe().getIp());
			Connection connection;
			try {
				connection = factory.newConnection();

				Channel channel = connection.createChannel();

				channel.exchangeDeclare(model.getNickname(), "fanout");

				JsonObject message = new JsonObject();
				message.addProperty("playerName", model.getNickname());
				String x = message.toString();

				channel.basicPublish(model.getNickname(), "Rules", null, x.getBytes());
				logger.fine(" [x] Sent '" + message + "'");
				if(model.getGAME_STATE() != GameState.IN_GAME){ //Since the queue may update the state before this does, this is needed.
					model.setGAME_STATE(GameState.WAITING_FOR_GAMESTART);
				}

				channel.close();
				connection.close();
			} catch (IOException | TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			JOptionPane.showMessageDialog(null,
				    "Every rule must be assigned.",
				    "Missing rule",
				    JOptionPane.ERROR_MESSAGE);
		}

	}

}
