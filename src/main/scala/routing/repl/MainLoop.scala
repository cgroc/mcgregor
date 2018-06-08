package routing.repl

import routing.amqp.{RoutingPublisher, RoutingSubscriber}

object MainLoop extends App {

  val routingSubscriber1: RoutingSubscriber = new RoutingSubscriber("Subscriber 1", List("white", "grey"))
  val routingSubscriber2: RoutingSubscriber = new RoutingSubscriber("Subscriber 2", List("black", "grey"))

  val daemon1 = new Thread(routingSubscriber1)
  val daemon2 = new Thread(routingSubscriber2)

  daemon1.start
  daemon2.start

  RoutingPublisher.publishMessage("Hello on white", "white")
  RoutingPublisher.publishMessage("Hello on black", "black")
  RoutingPublisher.publishMessage("Hello on grey", "grey")

  routingSubscriber1.blat
  routingSubscriber2.blat

}
