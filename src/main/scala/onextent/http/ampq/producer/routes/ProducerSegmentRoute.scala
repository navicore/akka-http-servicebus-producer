package onextent.http.ampq.producer.routes

import java.util.Date

import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.LazyLogging
import onextent.http.ampq.producer.ErrorSupport
import onextent.http.ampq.producer.io.servicebus.QueueWriter
import onextent.http.ampq.producer.models.{JsonSupport, Message}
import spray.json._

object ProducerSegmentRoute
    extends JsonSupport
    with LazyLogging
    with Directives
    with ErrorSupport {

  def apply: Route =
    path(urlpath / Segment) { name =>
      logRequest(s"$urlpath / $name") {
        handleErrors {
          cors(corsSettings) {
            get {
              val response =
                Message(java.util.UUID.randomUUID(), new Date(), s"hiya $name")
              complete(response.toJson.prettyPrint)
            } ~
              post {
                decodeRequest {
                  entity(as[Message]) { m =>
                    QueueWriter(name, m.body)
                    val response = Message(java.util.UUID.randomUUID(),
                                           new Date(),
                                           s"${m.body} to you, too!")
                    complete(response.toJson.prettyPrint)
                  }
                }
              }
          }
        }
      }
    }
}
