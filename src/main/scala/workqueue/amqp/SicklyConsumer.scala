package workqueue.amqp

import com.rabbitmq.client._

import scala.util.control.NonFatal

// receives a message then kicks the bucket without acking, just want to see rabbit requeue the messages!

class SicklyConsumer(_name: String) extends Runnable {

  val name = _name

  val QUEUE_NAME: String = "hello"

  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection
  val channel = connection.createChannel

  channel.queueDeclare(QUEUE_NAME, false, false, false, null)

  override def run(): Unit = {

    println(s" [*] Consumer $name waiting for messages (*cough cough*).")

    val consumer: Consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]) = {
        val message: String = new String(body, "UTF-8")
        println(s" [x] $name received '$message', processing...")
        println(s" [x] $name doesn't feel so good... urrrrghhh")
        channel.close()
        connection.close()
      }
    }

    channel.basicConsume(QUEUE_NAME, false, consumer)

  }

  def blat: Unit = {

    try {
      channel.close()
      connection.close()
    } catch {
      case NonFatal(err) =>
        println(s"Ouch!\n$err")
    }
  }
}
