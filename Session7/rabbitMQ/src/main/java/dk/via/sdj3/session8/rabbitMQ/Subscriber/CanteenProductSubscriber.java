package dk.via.sdj3.session8.rabbitMQ.Subscriber;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CanteenProductSubscriber
{
  private final static String QUEUE_NAME = "Via_Canteen_Products_Queue";

  public static void main(String[] args) throws IOException, TimeoutException
  {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    Consumer consumer = new DefaultConsumer(channel){
      @Override public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties,byte[] body)throws
          IOException
      {
        String message = new String(body, "UTF-8");
        System.out.println(" [Sub] Received '" + message + "'");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }
}
