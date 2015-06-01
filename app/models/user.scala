package models

import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat
import play.api.libs.json.{Json, Format}
/**
 * Created by Carlos on 31/05/2015.
 */
case class User(_id: Option[BSONObjectID], age: Int, firstName: String,lastName: String, active: Boolean, created: DateTime )

object JsonFormats{
  implicit val mongoFormat: Format[User] = Json.format[User]
}
