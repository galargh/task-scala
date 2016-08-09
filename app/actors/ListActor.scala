package actors

import akka.actor.{PoisonPill, Actor, Props}

import implicits.ListMath._

object ListActor {
  def props = Props[ListActor]
}
class ListActor extends Actor {
  import ListActor._

  lazy val list: List[Int] = List(3,1,4,1,5,9,2,6,5,8,9,7,9,3,2,3,8)

  // sends back an Either[Int, List] to the sender
  // kills the actor after finishing
  def receive: Receive = {
    case "sum" =>
      sender() ! Left(getSum)
      self ! PoisonPill
    case "median" =>
      sender() ! Left(getMedian)
      self ! PoisonPill
    case "average" =>
      sender() ! Left(getAverage)
      self ! PoisonPill
    case _ =>
      sender() ! Right(getList)
      self ! PoisonPill
  }

  def getList: List[Int] = {
    list
  }

  def getSum: Int = {
    getList.sum
  }

  def getMedian: Int = {
    getList.median.toInt
  }

  def getAverage: Int = {
    getList.average.toInt
  }
}
