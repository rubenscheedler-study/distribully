package distribully.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import distribully.model.DistribullyModel;

public class GameConsumerThread extends Thread{

	DistribullyModel model;
	String queueName;
	Channel channel;
	public GameConsumerThread(DistribullyModel model){
		this.model = model;
		ConnectionFactory factory = new ConnectionFactory(); 
		factory.setHost(model.getGamePlayerList().getPlayerByNickname(model.getCurrentHostName()).getIp());
		Connection connection = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(model.getCurrentHostName(), "fanout");
			queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, model.getCurrentHostName(), "");

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run(){

	}

}
