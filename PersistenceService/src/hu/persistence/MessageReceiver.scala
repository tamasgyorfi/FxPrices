package hu.persistence

import java.util.Scanner

import hu.persistence.requestprocessor.AbstractMessageReceiver
import hu.persistence.requestprocessor.AbstractMessageReceiver
import hu.persistence.requestprocessor.LoggingMessageReceiver

object ReceiverType extends Enumeration {
	type ReceiverType = Value
			val LOGGING, DATABASE = Value
}

object MessageReceiverFactory {

  def apply(receiverType: ReceiverType.Value): AbstractMessageReceiver = {
    if (receiverType == ReceiverType.LOGGING) {
      new LoggingMessageReceiver
    } else {
      null // No impl yet
    }
  }

  def main(args: Array[String]) = {
    val receiver = {
      MessageReceiverFactory(ReceiverType.LOGGING).listenOnBackgroundThread
      do {
      } while (true)
    }
  }
}