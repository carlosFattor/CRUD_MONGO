package controllers

import javax.inject.Singleton
import org.slf4j.{LoggerFactory, Logger}
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import models.{User}
import scala.concurrent.Future
import models.JsonFormats._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.api.Cursor

/**
 * Created by Carlos on 31/05/2015.
 */
@Singleton
class Users extends Controller with MongoController {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Users])

  def collection: JSONCollection = db.collection[JSONCollection]("users")

  def createUser = Action.async(BodyParsers.parse.json) {
    request =>
      request.body.validate[User].map{
        user =>
          collection.insert(user).map{
            lastError =>
              logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(s"User Created")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def updateUser(firstName: String, lastName: String) = Action.async(parse.json){
    request =>
      request.body.validate[User].map{
        user =>
          val nameSelector = Json.obj("firstName" -> firstName, "lastName" -> lastName)
          collection.update(nameSelector, user).map{
            lastError =>
              logger.debug(s"Successfully updated with LastError: $lastError")
              Created(s"User updated")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findUsers = Action.async {
    val cursor: Cursor[User] = collection
      .find(Json.obj("active" -> true))
      .sort(Json.obj("created" -> -1))
      .cursor[User]

    val futureUsersList: Future[List[User]] = cursor.collect[List]()
    val futurePersonsJsonArray: Future[JsArray] = futureUsersList.map {
      users =>
        Json.arr(users)
    }
    futurePersonsJsonArray.map{
      users =>
        Ok(users(0))
    }
  }
}
