package workqueue.repl

import workqueue.amqp.{SlowConsumer, BasicPublisher}

import scala.io.StdIn._

object MainLoop extends App {

  val consumer: SlowConsumer = new SlowConsumer("Drone")

  val consumerDaemon = new Thread(consumer)

  consumerDaemon.start

  def handleMessage(msg: String): Unit = BasicPublisher.publishMessage(msg)

  def loop: Unit = {
    val message = readLine("What do you want to say?: ")
    if (message equals ":q") {
      consumer.blat
    } else {
      handleMessage(message)
      loop
    }
  }

  println("Enter a message or type ':q' to quit")
  loop
}
