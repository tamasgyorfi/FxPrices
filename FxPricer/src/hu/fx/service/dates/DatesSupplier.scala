package hu.fx.service.dates

import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

object DatesSupplier extends DateComponent{

  private val calendar = Calendar.getInstance
  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+0000")
  
  def currentDate() = calendar.getTime
  def dateFormatted(date: Date) = dateFormat.format(date)
}