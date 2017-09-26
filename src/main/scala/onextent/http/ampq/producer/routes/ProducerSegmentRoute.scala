package onextent.http.ampq.producer.routes

import java.util.Date

import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.LazyLogging
import onextent.http.ampq.producer.ErrorSupport
import onextent.http.ampq.producer.io.servicebus.QueueWriter
import onextent.http.ampq.producer.models.{JsonSupport, Message}
import spray.json._

import scala.concurrent.Future

object ProducerSegmentRoute
    extends JsonSupport
    with LazyLogging
    with Directives
    with ErrorSupport
    with QueueWriter {

  def apply: Route =
    path(urlpath / Segment) { name =>
      logRequest(s"$urlpath / $name") {
        handleErrors {
          cors(corsSettings) {
            post {
              decodeRequest {
                entity(as[Message]) { m =>
                  writeQueue(m.body, name) { (f: Future[Void]) =>
                    onSuccess(f) { _ =>
                      val response = Message(java.util.UUID.randomUUID(),
                                             new Date(),
                                             s"${m.body}")
                      complete(response.toJson.prettyPrint)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
}
