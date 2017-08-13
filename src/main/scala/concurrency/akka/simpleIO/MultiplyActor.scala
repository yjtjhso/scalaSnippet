package concurrency.akka.simpleIO

import akka.actor.{Actor, ActorRef, Props}
import com.typesafe.scalalogging.slf4j.StrictLogging
import concurrency.akka.simpleIO.GreetingActor.{GreetResp, GreetingReq}
import concurrency.akka.simpleIO.MultiplyActor.NumberInput
import concurrency.akka.simpleIO.ToUpperActor.{ToUpperReq, ToUpperResp}

object MultiplyActor {
  def props(): Props = Props(new MultiplyActor)

  case class NumberInput(num: Int)
}

class MultiplyActor extends Actor with StrictLogging {
  val toUpperActor: ActorRef = context.actorOf(ToUpperActor.props())
  val greetingActor: ActorRef = context.actorOf(GreetingActor.props())
  var asker: Option[ActorRef] = None

  override def receive: Receive = {
    case NumberInput(i) => {
      asker = Some(sender())
      toUpperActor ! ToUpperReq(i.toString)
    }
    case ToUpperResp(v) => {
      greetingActor ! GreetingReq(v)
    }
    case GreetResp(v) => {
//      asker.foreach(_ ! v)
      logger.info(v)
    }
  }
}