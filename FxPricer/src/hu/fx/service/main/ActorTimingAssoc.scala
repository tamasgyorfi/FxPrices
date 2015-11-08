package hu.fx.service.main

import akka.actor.ActorRef

class ActorTimingAssoc(val actorRef: ActorRef, val timing: scala.concurrent.duration.FiniteDuration) {
}