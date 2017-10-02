package onextent.http.servicebus.producer.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directives, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging
import onextent.http.servicebus.producer.ErrorSupport
import onextent.http.servicebus.producer.models.JsonSupport

object ProducerRoute
    extends JsonSupport
    with LazyLogging
    with Directives
    with ErrorSupport {

  def apply: Route =
    path(urlpath) {
      logRequest(urlpath) {
        handleErrors {
          cors(corsSettings) {
            get {
              logger.debug(s"get $urlpath")
              complete(
                HttpEntity(ContentTypes.`application/json`,
                           "{\"msg\": \"Say hello to AKKA HTTP AMQP Producer\"}\n"))
            }
          }
        }
      }
    }
}
