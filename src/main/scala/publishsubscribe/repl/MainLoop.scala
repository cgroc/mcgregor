package publishsubscribe.repl

import publishsubscribe.amqp.{ExchangePublisher, Subscriber}

import scala.io.StdIn.readLine

object MainLoop extends App {

  val subscriber1: Subscriber = new Subscriber("Subscriber 1")
  val subscriber2: Subscriber = new Subscriber("Subscriber 2")
  val subscriber3: Subscriber = new Subscriber("Subscriber 3")


  val subscriberDaemon1 = new Thread(subscriber1)
  val subscriberDaemon2 = new Thread(subscriber2)
  val subscriberDaemon3 = new Thread(subscriber3)

  subscriberDaemon1.start()
  subscriberDaemon2.start()
  subscriberDaemon3.start()

  def handleMessage(msg: String): Unit = ExchangePublisher.publishMessage(msg)

  def loop: Unit = {
    val message = readLine("What do you want to say?: ")
    if (message equals ":q") {
      subscriber1.blat
      subscriber2.blat
      subscriber3.blat
    } else {
      handleMessage(message)
      loop
    }
  }

  println("Enter a message or type ':q' to quit")
  loop

}
