package hu.fx.service

import scala.actors.threadpool.TimeUnit

class SchedulingInformation(val delay: Long, val unit: TimeUnit) {}

object SchedulingInformation {
  def apply(delay: Long, unit: TimeUnit) = {
    new SchedulingInformation(delay, unit)
  }
}