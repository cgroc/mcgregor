package workqueue.amqp

import com.rabbitmq.client._


class SlowConsumer(_name: String) extends Runnable {

  val name = _name

  val QUEUE_NAME: String = "hello"

  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection = factory.newConnection
  val channel = connection.createChannel

  channel.queueDeclare(QUEUE_NAME, false, false, false, null)

  override def run(): Unit = {

    println(s" [*] Consumer $name waiting for messages.")

    val consumer: Consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]) = {
        val message: String = new String(body, "UTF-8")
        println(s" [x] $name received '$message', processing...")
        Thread.sleep(3000)
        println(s" [x] $name done!")
      }
    }

    channel.basicConsume(QUEUE_NAME, true, consumer)

  }

  def blat: Unit = {

    channel.close()
    connection.close()
  }
}
