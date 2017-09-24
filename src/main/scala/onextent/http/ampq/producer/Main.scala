package onextent.http.ampq.producer

import java.util.Date
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging
import onextent.http.ampq.producer.models.{JsonSupport, Message}
import onextent.http.ampq.producer.routes.{ProducerRoute, ProducerSegmentRoute}
import spray.json._
import scala.concurrent.ExecutionContextExecutor

object Main extends LazyLogging with JsonSupport with ErrorSupport {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("hello-world-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route =
      HealthCheck ~
      ProducerRoute.apply ~
      ProducerSegmentRoute.apply

    Http().bindAndHandle(route, "0.0.0.0", port)
  }
}

