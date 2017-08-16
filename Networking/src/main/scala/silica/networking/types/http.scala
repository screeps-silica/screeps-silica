package silica.networking.types

import io.circe.Encoder, io.circe.Json
import io.circe.generic.extras._
import io.circe.syntax._

object HttpDefs {
  import silica.networking.types.util.EncodeEither._

  implicit val config: Configuration = Configuration.default.withDefaults

  /** Input sub-structure of various calls */
  @ConfiguredJsonCodec case class CustomBadge(
    color1: String,
    color2: String,
    color3: String,
    flip: Boolean,
    param: Int,
    @JsonKey("type") tpe: Int
  )

  /** Input sub-structure of my-info */
  @ConfiguredJsonCodec case class NotifyPrefs(
    errorsInterval: Int,
  )

  /** Input sub-structure of my-info */
  @ConfiguredJsonCodec case class SteamInfo(
    displayName: String,
    id: String,
    ownership: List[Int],
  )

  /** Input from a my-info call */
  @ConfiguredJsonCodec case class MyInfo(
    @JsonKey("_id") id: String,
    email: String,
    username: String,
    password: Boolean,
    cpu: Int,
    gcl: Long,
    money: Double,
    credits: Option[Double],
    badge: Either[Int, CustomBadge],
    notifyPrefs: NotifyPrefs,
    promoPeriodUntil: Option[Long],
    steam: Option[SteamInfo],
    subscription: Option[Boolean],
    subscriptionTokens: Option[Int],
  )

  /** Output details for a login call */
  @ConfiguredJsonCodec case class LoginDetails(
    email: String,
    password: String,
  )

  /** Input from a login call */
  @ConfiguredJsonCodec case class LoggedIn(
    token: String,
  )

  /** Output sub-structure of map-stats */
  @ConfiguredJsonCodec sealed trait MapStatName
  object MapStatName {
    final case object RoomOwner extends MapStatName
    final case object Claim extends MapStatName
  }

  implicit val encodeMapStatName = new Encoder[MapStatName] {
    final def apply(a: MapStatName): Json = a match {
      case MapStatName.RoomOwner => Json.fromString("owner0")
      case MapStatName.Claim => Json.fromString("claim0")
    }
  }

  /** Output details for map-stats call */
  @ConfiguredJsonCodec case class MapStatsDetails(
    rooms: List[String],
    stat: MapStatName,
    shard: String
  )

  /** Input from a map-stats call */
  @ConfiguredJsonCodec case class MapStats(
    stats: Map[String, MapStatsRoomResponse],
    users: Map[String, MapStatsUserResponse]
  )

  /** Input sub-structure of map-stats */
  @ConfiguredJsonCodec case class MapStatsRoomResponse(
    // TODO: parse status, novice, and open_time keys into a "room state" sealed trait
    // (nonexistant, closed, open, novice{end_time} or second_tier_novice{room_open_time, end_time})
    status: String,
    @JsonKey("own") owner: Option[MapStatsRoomOwner],
    // TODO: parse these into a scala time type - the json can
    // be either a string contains a number, or a number itself - both represent unix time
    novice: Option[Either[Long, String]],
    openTime: Option[Either[Long, String]],
    sign: Option[RoomSign],
    hardSign: Option[HardSign],
  )

  /** Input sub-structure of map-stats */
  @ConfiguredJsonCodec case class MapStatsUserResponse(
    badge: Either[Int, CustomBadge],
    @JsonKey("_id") user_id: String,
    username: String,
  )

  /** Input sub-structure of map-stats */
  @ConfiguredJsonCodec case class MapStatsRoomOwner(
    @JsonKey("user") userId: String,
    @JsonKey("level") roomControllerLevel: Int
  )

  /** Input sub-structure of various calls */
  @ConfiguredJsonCodec case class RoomSign(
    @JsonKey("time") gameTimeSet: Int,
    @JsonKey("datetime") timeSet: Long, // TODO: scala unix time type
    @JsonKey("user") userId: String,
    text: String,
  )

  /** Input sub-structure of various calls */
  @ConfiguredJsonCodec case class HardSign(
    @JsonKey("time") gameTimeSet: Int,
    @JsonKey("datetime") start: Long, // TODO: scala unix time type
    @JsonKey("endDatetime") end: Long, // TODO: scala unix time type
    text: String,
  )
}
