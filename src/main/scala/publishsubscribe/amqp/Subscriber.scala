package publishsubscribe.amqp

import com.rabbitmq.client._

import scala.util.control.NonFatal

class Subscriber(_name: String) extends Runnable {

  val name = _name

  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection
  val channel = connection.createChannel

  channel.exchangeDeclare("logs", "fanout") // is there a constant somewhere for this?
  // before we were doing this
  // channel.queueDeclare(QUEUE_NAME, false, false, false, null)

  val QUEUE_NAME: String = channel.queueDeclare().getQueue

  channel.queueBind(QUEUE_NAME, "logs", "")


  override def run(): Unit = {

    println(s" [*] Consumer $name waiting for messages.")

    val consumer: Consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]) = {
        val message: String = new String(body, "UTF-8")
        println(s" [x] $name received '$message', logging...")
      }
    }

    channel.basicConsume(QUEUE_NAME, true, consumer)

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