package org.netcomputing.rabbitmq.helloworld;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;

public class Receiver {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv)
			throws java.io.IOException,
			java.lang.InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("82.73.233.237");
		Connection connection = null;
		try {
			connection = factory.newConnection();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//		ConnectionFactory factory2 = new ConnectionFactory();
//		factory2.setHost("82.72.30.166");
//		Connection connection2 = null;
//		try {
//			connection2 = factory2.newConnection();
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		channel = connection2.createChannel();
//		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		

		//QueueingConsumer consumer = new QueueingConsumer(channel);
		//channel.basicConsume(QUEUE_NAME, true, consumer);
		int i = 0;
		while(true){
			GetResponse response = channel.basicGet(QUEUE_NAME, true);
			if (response == null) { 

			}else{
				System.out.println(" [" + i + "] Received '" + new String(response.getBody()) + "'");
				i++;
			}

		}
		//	    while (true) {
		//	      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		//	      String message = new String(delivery.getBody());
		//	      System.out.println(" [x] Received '" + message + "'");
		//	    }

	}
}
