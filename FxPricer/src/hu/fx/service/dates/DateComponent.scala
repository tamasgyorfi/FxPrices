package hu.fx.service.dates

import java.util.Date

trait DateComponent {

  def dateFormatted(date : Date): String
  def currentDate() : Date
}