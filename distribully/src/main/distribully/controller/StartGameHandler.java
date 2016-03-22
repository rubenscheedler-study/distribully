package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.DistribullyModel;

public class StartGameHandler implements ActionListener  {

	private DistribullyModel model;
	private static Logger logger;
	public StartGameHandler(DistribullyModel model) {
		this.model = model;
		logger = Logger.getLogger("controller.StartGameHandler");
		logger.setParent(Logger.getLogger("controller.DistribullyController"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		DistribullyController.InviteThreadList.forEach(thread -> thread.closeServer());
		DistribullyController.updateGameHostThread.setIsSettingUpGame(false);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(model.getMe().getIp());
		Connection connection;
		try {
			connection = factory.newConnection();

			Channel channel = connection.createChannel();

			channel.exchangeDeclare(model.getNickname(), "fanout");

			channel.basicPublish(model.getNickname(), "Start", null, null);
			logger.fine("Sent gameStart");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
