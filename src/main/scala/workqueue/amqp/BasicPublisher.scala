package workqueue.amqp

import com.rabbitmq.client.ConnectionFactory

object BasicPublisher {

  val QUEUE_NAME: String = "hello"

  def publishMessage(message: String): Unit = {
    val factory = new ConnectionFactory

    factory.setHost("localhost") //bleugh!

    val connection = factory.newConnection

    val channel = connection.createChannel

    //Send!

    channel.queueDeclare(QUEUE_NAME, false, false, false, null)

    channel.basicPublish("", QUEUE_NAME, null, message.getBytes)
    System.out.println(" [x] Sent '" + message + "'")

    // deal with resources

    channel.close()

    connection.close()

  }




}
