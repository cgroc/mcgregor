package workqueue.repl

import workqueue.amqp.{BasicPublisher, SicklyConsumer, SlowConsumer}

import scala.io.StdIn._

object MainLoop extends App {

  val sicklyConsumer: SicklyConsumer = new SicklyConsumer("Darren Anderton")
  val consumer1: SlowConsumer = new SlowConsumer("Drone 1")
  val consumer2: SlowConsumer = new SlowConsumer("Drone 2")
  val consumer3: SlowConsumer = new SlowConsumer("Drone 3")

  val sicklyDaemon = new Thread(sicklyConsumer)
  val consumerDaemon1 = new Thread(consumer1)
  val consumerDaemon2 = new Thread(consumer2)
  val consumerDaemon3 = new Thread(consumer3)

  sicklyDaemon.start()
  consumerDaemon1.start()
  consumerDaemon2.start()
  consumerDaemon3.start()

  def handleMessage(msg: String): Unit = BasicPublisher.publishMessage(msg)

  def loop: Unit = {
    val message = readLine("What do you want to say?: ")
    if (message equals ":q") {
      sicklyConsumer.blat
      consumer1.blat
      consumer2.blat
      consumer3.blat
    } else {
      handleMessage(message)
      loop
    }
  }

  println("Enter a message or type ':q' to quit")
  loop
}
