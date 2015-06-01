import com.google.inject.{Guice, AbstractModule}
import play.api.GlobalSettings
import services.{SimpleUUIDGenerator, UUIDGenerator}

/**
 * Created by Carlos on 31/05/2015.
 */
object Global extends GlobalSettings {

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[UUIDGenerator]).to(classOf[SimpleUUIDGenerator])
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

}
