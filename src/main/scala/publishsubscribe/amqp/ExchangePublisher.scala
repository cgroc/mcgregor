package publishsubscribe.amqp

import com.rabbitmq.client.ConnectionFactory

object ExchangePublisher {

  def publishMessage(message: String): Unit = {
    val factory = new ConnectionFactory

    factory.setHost("localhost") //bleugh!

    val connection = factory.newConnection

    val channel = connection.createChannel

    //Send!

//    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    channel.exchangeDeclare("logs", "fanout") // is there a constant somewhere for this?

    channel.basicPublish("logs", "", null, message.getBytes)
    System.out.println(" [x] Sent '" + message + "'")

    // deal with resources

    channel.close()

    connection.close()

  }




}
