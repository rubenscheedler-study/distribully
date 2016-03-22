package distribully.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.Player;

public class ProducerHandler {
	private static Logger logger;

	public ProducerHandler(String message, String key, Player producer){
		logger = LoggerFactory.getLogger("controller.ProducerHandler");
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(producer.getIp());
		Connection connection;
		try {
			connection = factory.newConnection();

			Channel channel = connection.createChannel();

			channel.exchangeDeclare(producer.getName(), "fanout");

			channel.basicPublish(producer.getName(), key, null, message.getBytes());
			logger.info(" [x] Sent '" + message + "'");
			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e1) {
			JOptionPane.showMessageDialog(null,
				    "Error while sending message to other users.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			//Doesn't matter what happens here, since we are closing the application anyway. A log for debugging will suffice.
			logger.error("Error while sending message: " + message);			
		}
	}
}
