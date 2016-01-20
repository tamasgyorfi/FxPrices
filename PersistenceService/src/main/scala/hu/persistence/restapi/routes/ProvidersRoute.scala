package hu.persistence.restapi.routes

import spray.http.MediaTypes.`application/json`
import hu.persistence.restapi.PriceReply
import hu.persistence.restapi.MaxPriceRequest
import akka.pattern.ask
import hu.persistence.restapi.ProvidersReply
import hu.persistence.restapi.ProvidersRequest

trait ProvidersRoute extends Route {

  def providersRoute = {
    get {
      respondWithMediaType(`application/json`) {
        path("providers") {
          {
            complete {
              (newWorker ? ProvidersRequest).mapTo[ProvidersReply].map { reply => ObjMapper.objectMapper.writeValueAsString(reply) }
            }
          }
        }
      }
    }
  }

}