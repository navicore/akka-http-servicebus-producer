package onextent.http.ampq.producer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import onextent.http.ampq.producer.models.JsonSupport
import onextent.http.ampq.producer.routes.{ProducerRoute, ProducerSegmentRoute}
import scala.concurrent.ExecutionContextExecutor

object Main extends LazyLogging with JsonSupport with ErrorSupport {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("amqp-http-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route =
      HealthCheck ~
      ProducerRoute.apply ~
      ProducerSegmentRoute.apply

    Http().bindAndHandle(route, "0.0.0.0", port)
  }
}

