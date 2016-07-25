package actors

import akka.actor.{PoisonPill, Actor}

class ListActor extends Actor {

    lazy val list: List[Int] = List(3,1,4,1,5,9,2,6,5,8,9,7,9,3,2,3,8)

    // sends back an Either[Int, List] to the sender
    def receive: Receive = {
        case "sum" =>
            // return sum of list
            sender() ! Left(0)
            self ! PoisonPill
        case "median" =>
            // return median of list
            sender() ! Left(0)
            self ! PoisonPill
        case "average" =>
            // return average of list
            sender() ! Left(0)
            self ! PoisonPill
        case _ =>
            sender() ! Right(getList)
            self ! PoisonPill
    }

    def getList: List[Int] = {
        list
    }
}
