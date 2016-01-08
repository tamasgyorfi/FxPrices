package hu.fx.service.price

import akka.actor.ActorRef

class ServiceRefreshAssoc(val actorRef: ActorRef, val timing: scala.concurrent.duration.FiniteDuration) {
}