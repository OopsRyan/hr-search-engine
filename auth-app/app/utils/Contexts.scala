package utils

import akka.actor.ActorSystem
import javax.inject.Inject
import play.Application

@Singleton
class Contexts @Inject()(akkaSystem: ActorSystem, configuration: play.api.Configuration) {
  implicit val dbLookup = akkaSystem.dispatchers.lookup("contexts.db-lookups")
  implicit val cpuLookup = akkaSystem.dispatchers.lookup("contexts.cpu-operations")

  val tokenTTL = configuration.get[Long]("token.ttl")
}
