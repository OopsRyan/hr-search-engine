package json

import play.api.libs.json._
import play.api.libs.functional.syntax._


object JsonReader {

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

  def main(arg: Array[String]) = {

    println(readTeamName(jValue))
    println(readNames(jValue))

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

    macroTest(jValue)
    
  }

  // Handy way
  def macroTest(value: JsValue = jValue) = {
    implicit val playerReads: Reads[Player] = Json.reads[Player]
    implicit val locationReads = Json.reads[Location]
    implicit val teamReads = Json.reads[Team]

    val teams = value.as[Team]
    teams
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
}


case class Team(teamName: String, players: List[Player], location: Location)
case class Player(name: String, age: Int)
case class Location(lat: Double, long: Double)


object JsonWriter {

  def main(args: Array[String]): Unit = {
    val json: JsValue = Json.obj(
      "teamName" -> "Real Madrid FC",
      "players" -> Json.arr(
        Json.obj("name" -> JsString("Ronaldo"), "age" -> JsNumber(32)),
        Json.obj("name" -> JsString("Modric"), "age" -> JsNumber(28)),
        Json.obj("name" -> JsString("Bale"), "age" -> 23)
      ),
      "location" -> Json.obj("lat" -> 40.2233, "long" -> 2.3212)
    )

    println(json.toString())

    macroTest(JsonReader.macroTest())
  }

  def macroTest(teams: Team) = {
    implicit val playerWrites = Json.writes[Player]
    implicit val locationWrites = Json.writes[Location]
    implicit val positionWrites = Json.writes[Team]

    println(Json.prettyPrint(Json.toJson(teams)))
  }
}