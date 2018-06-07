package hello.amqp

import com.rabbitmq.client._


class BasicConsumer extends Runnable {


  val QUEUE_NAME: String = "hello"

  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection
  val channel = connection.createChannel

  channel.queueDeclare(QUEUE_NAME, false, false, false, null)

  override def run(): Unit = {

    System.out.println(" [*] Consumer waiting for messages.")

    val consumer: Consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]) = {
        val message: String = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    }

    channel.basicConsume(QUEUE_NAME, true, consumer)

  }

  def blat: Unit = {

    channel.close()
    connection.close()
  }
}
