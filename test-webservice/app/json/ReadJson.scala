package json

import play.api.libs.json._
import play.api.libs.functional.syntax._

class ReadJson {

  val jsonString =
    """{
      | "teamName" : "Real Madrid FC",
      | "players" : [
      |   {
        |   "name" : "Ronaldo",
        |   "age" : 35
      |   },
      |   {
      |     "name" : "Modric",
      |     "age" : 12
      |   }
      | ],
      | "location" : {
      |   "lat" : 40.5412,
      |   "long" : 3.22212
      | }
      |}""".stripMargin

  val jValue: JsValue = Json.parse(jsonString)
}


object JsonRunner {

  def main(arg: Array[String]) = {

    println(readTeamName(new ReadJson().jValue))
    println(readNames(new ReadJson().jValue))

//    implicit val locationReads: Reads[Location] = (
//      (JsPath \ "lat").read[Double] and
//        (JsPath \ "long").read[Double]
//      )(Location.apply _ )
//
//    implicit val playerReads: Reads[Player] = (
//      (JsPath \ "name").read[String] and
//        (JsPath \ "age").read[Int]
//    )(Player.apply _)
//
//    implicit val teamReads: Reads[Team] = (
//      (JsPath \ "teamName").read[String] and
//        (JsPath \ "players").read[List[Player]] and
//        (JsPath \ "location").read[Location]
//    )(Team.apply _)
//
//    println(new ReadJson().jValue.as[Team])

    macroTest(new ReadJson().jValue)
    
  }

  // Handy way
  def macroTest(value: JsValue) = {
    implicit val playerReads: Reads[Player] = Json.reads[Player]
    implicit val locationReads = Json.reads[Location]
    implicit val teamReads = Json.reads[Team]

    val teams = value.as[Team]
    println(teams)
  }

  def readTeamName(value: JsValue) = {

    val validation: JsResult[String] = (value \ "teamName").validate[String]

    validation match {
      case x: JsSuccess[String] => x.get
      case e: JsError => e.errors
    }
  }

  def readNames(value: JsValue) = {
    val results: Seq[JsValue] = (value \\ "name")

    results.map(_.validate[String] match {
      case x: JsSuccess[String] => x.get
      case e: JsError => e.errors
    })
  }

  case class Team(teamName: String, players: List[Player], location: Location)
  case class Player(name: String, age: Int)
  case class Location(lat: Double, long: Double)




}