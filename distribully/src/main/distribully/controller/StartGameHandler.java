package distribully.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.DistribullyModel;

public class StartGameHandler implements ActionListener  {

	private DistribullyModel model;
	private static Logger logger;
	public StartGameHandler(DistribullyModel model) {
		this.model = model;
		logger = LoggerFactory.getLogger("controller.StartGameHandler");
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
			logger.info("Sent gameStart");

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			JOptionPane.showMessageDialog(null,
				    "Game creation failed.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			logger.error("Game creation failed");
			new BackToMainPageHandler(model);
		}
	}

}
