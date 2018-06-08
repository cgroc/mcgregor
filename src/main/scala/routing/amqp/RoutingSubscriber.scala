package routing.amqp

import com.rabbitmq.client._

import scala.util.control.NonFatal

class RoutingSubscriber(_name: String, _routingKeys: List[String]) extends Runnable {

  val name = _name
  val routingKeys = _routingKeys

  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection
  val channel = connection.createChannel

  channel.exchangeDeclare("routing", "direct")

  val QUEUE_NAME: String = channel.queueDeclare().getQueue

  routingKeys.map(key => channel.queueBind(QUEUE_NAME, "routing", key)) // bind to each routing queue


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
