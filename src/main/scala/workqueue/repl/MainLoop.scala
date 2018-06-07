package workqueue.repl

import workqueue.amqp.{SlowConsumer, BasicPublisher}

import scala.io.StdIn._

object MainLoop extends App {

  val consumer1: SlowConsumer = new SlowConsumer("Drone 1")
  val consumer2: SlowConsumer = new SlowConsumer("Drone 2")

  val consumerDaemon1 = new Thread(consumer1)
  val consumerDaemon2 = new Thread(consumer2)

  consumerDaemon1.start
  consumerDaemon2.start

  def handleMessage(msg: String): Unit = BasicPublisher.publishMessage(msg)

  def loop: Unit = {
    val message = readLine("What do you want to say?: ")
    if (message equals ":q") {
      consumer1.blat
      consumer2.blat
    } else {
      handleMessage(message)
      loop
    }
  }

  println("Enter a message or type ':q' to quit")
  loop
}
