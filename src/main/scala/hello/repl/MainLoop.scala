package hello.repl

import hello.amqp.{BasicConsumer, BasicPublisher}

import scala.io.StdIn._

object MainLoop extends App {

  val consumer: BasicConsumer = new BasicConsumer

  val consumerDaemon = new Thread(consumer)

  consumerDaemon.start

  def handleMessage(msg: String): Unit = BasicPublisher.publishMessage(msg)

  def loop: Unit = {
    val message = readLine("What do you want to say?: ")
    if (message equals ":q") {
      consumer.blat
      consumerDaemon.stop() // this is deprecated, how can I do this nicely?
    } else {
      handleMessage(message)
      loop
    }
  }

  println("Enter a message or type ':q' to quit")
  loop
}
