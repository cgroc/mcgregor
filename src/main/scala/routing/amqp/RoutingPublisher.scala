package routing.amqp

import com.rabbitmq.client.ConnectionFactory

object RoutingPublisher {

  def publishMessage(message: String, routingKey: String): Unit = {
    val factory = new ConnectionFactory

    factory.setHost("localhost") //bleugh!

    val connection = factory.newConnection

    val channel = connection.createChannel

    //Send!

    //    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    channel.exchangeDeclare("routing", "direct") // is there a constant somewhere for this?

    channel.basicPublish("routing", routingKey, null, message.getBytes)
    System.out.println(" [x] Sent '" + message + "'")

    // deal with resources

    channel.close()

    connection.close()

  }


}
