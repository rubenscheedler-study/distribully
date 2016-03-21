package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.DistribullyModel;

public class FinishSelectRulesHandler  implements ActionListener {

	private DistribullyModel model;
	public FinishSelectRulesHandler(DistribullyModel model) {
	this.model = model;	
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(model.getMe().getIp());
		Connection connection;
		try {
			connection = factory.newConnection();

			Channel channel = connection.createChannel();

			channel.exchangeDeclare(model.getNickname(), "fanout");

			JsonObject message = new JsonObject();
			message.addProperty("playerName", model.getNickname());

			channel.basicPublish(model.getNickname(), "Rules", null, message.toString().getBytes());
			System.out.println(" [x] Sent '" + message + "'");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
