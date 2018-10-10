package com.ryan.search

import play.api.libs.json.Json


abstract class SUserResult {
  def score: Float
  def tag: String
  def location: String
}

case class SearchFilter(location: Option[String], tag: Option[String])
object SearchFilter {
  implicit val format = Json.format[SearchFilter]
}

case class SOUser(id: Long, name: String, soAccountId: Long, aboutMe: String,
                  soLink: String="#", location: String)
object SOUser {
  implicit val soUserJSON = Json.format[SOUser]
}

case class SOTag(id: Int, name: String)
object SOTag {
  implicit val soTagJSON = Json.format[SOTag]
}

case class SOUserScore(user: SOUser, map: Map[SOTag, Int])

case class SOSearchResult(override val score: Float, sOTag: SOTag, sOUser: SOUser)
  extends SUserResult {

  override def location: String = sOUser.location
  override def tag: String = sOTag.name
}

object SOSearchResult {
  implicit val soSearchResultJSON = Json.format[SOSearchResult]
}