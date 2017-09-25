package onextent.http.ampq.producer.io.servicebus

import com.microsoft.azure.servicebus._
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

object QueueWriter extends LazyLogging {

  val conf: Config = ConfigFactory.load()
  val connectionString: String = conf.getString("main.serviceBusConnectionString")

  def apply(queueName: String, message: String): Unit = {

    val queueClient = new QueueClient(new ConnectionStringBuilder(connectionString, queueName), ReceiveMode.RECEIVEANDDELETE)
    queueClient.sendAsync(new Message(message)).thenRunAsync(() => { logger.info("sent")})
  }

}
