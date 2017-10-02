package onextent.http.servicebus.producer.io.servicebus

import akka.http.scaladsl.server.{Directive1, Directives}
import com.microsoft.azure.servicebus._
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder
import com.typesafe.scalalogging.LazyLogging
import onextent.http.servicebus.producer.ErrorSupport

import scala.compat.java8.FutureConverters
import scala.concurrent.Future

trait QueueWriter extends LazyLogging with Directives with ErrorSupport {

  val connectionString: String = conf.getString("main.serviceBusConnectionString")
  val defaultQueue: String = conf.getString("main.defaultQueue")

  def writeQueue(message: String, queueName: String = defaultQueue ): Directive1[Future[Void]] = {
    val queueClient = new QueueClient(new ConnectionStringBuilder(connectionString, queueName), ReceiveMode.RECEIVEANDDELETE)
    provide(FutureConverters.toScala(queueClient.sendAsync(new Message(message))))
  }
}
