package controllers

import javax.inject.{Singleton, Inject}
import services.UUIDGenerator
import org.slf4j.{LoggerFactory, Logger}
import play.api.mvc._

@Singleton
class Application @Inject() (uuidGenerator: UUIDGenerator) extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])

  def index = Action {
    logger.info("Serving index page...")
    Ok(views.html.index("Your new application is ready."))
  }

  def randomUUID = Action {
    logger.info("calling UUIDGenerator")
    Ok(uuidGenerator.generate.toString)
  }
}
