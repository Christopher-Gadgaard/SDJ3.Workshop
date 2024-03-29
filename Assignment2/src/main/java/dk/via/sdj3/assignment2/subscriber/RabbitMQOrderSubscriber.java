package dk.via.sdj3.assignment2.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import dk.via.sdj3.assignment2.model.Order;


import java.io.IOException;

import java.util.concurrent.TimeoutException;

public class RabbitMQOrderSubscriber
{
  private static final String QUEUE_NAME = "x.sdj3.order.queue";

  public static void main(String[] args) throws IOException, TimeoutException
  {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    Consumer consumer = new DefaultConsumer(channel)
    {
      @Override public void handleDelivery(String consumerTag,
          Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException
      {
        String message = new String(body, "UTF-8");
        System.out.println("Received: " + message);

        Order order = new ObjectMapper().readValue(message, Order.class);
        System.out.println("Order: " + order);
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }
}
