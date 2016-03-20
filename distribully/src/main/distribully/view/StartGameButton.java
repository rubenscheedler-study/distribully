package distribully.view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import distribully.model.DistribullyModel;

public class StartGameButton extends JButton {

	private static final long serialVersionUID = -3732106117892923335L;

	private DistribullyModel model;

	public StartGameButton(DistribullyModel model) {
		this.setText("Start Game");
		this.model = model;
		this.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ConnectionFactory factory = new ConnectionFactory();
				factory.setHost(model.getMe().getIp());
				Connection connection;
				try {
					connection = factory.newConnection();

					Channel channel = connection.createChannel();

					channel.exchangeDeclare(model.getNickname(), "fanout");

					String message = "HELLO";

					channel.basicPublish(model.getNickname(), "Start", null, message.getBytes());
					System.out.println(" [x] Sent '" + message + "'");

					channel.close();
					connection.close();
				} catch (IOException | TimeoutException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}
