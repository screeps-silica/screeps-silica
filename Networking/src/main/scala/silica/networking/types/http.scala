package silica.networking.types

import io.circe.Encoder, io.circe.Json
import io.circe.generic.extras._
import io.circe.syntax._
import org.joda.time.DateTime

object HttpDefs {
  import silica.networking.types.util.EncodeDateTime._
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

  implicit val encodeMapStatName: Encoder[MapStatName] = {
    case MapStatName.RoomOwner => Json.fromString("owner0")
    case MapStatName.Claim => Json.fromString("claim0")
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
    novice: Option[DateTime],
    openTime: Option[DateTime],
    sign: Option[RoomSign],
    hardSign: Option[HardSign],
  )

  /** Input sub-structure of map-stats */
  @ConfiguredJsonCodec case class MapStatsUserResponse(
    badge: Either[Int, CustomBadge],
    @JsonKey("_id") userId: String,
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
    @JsonKey("datetime") timeSet: DateTime,
    @JsonKey("user") userId: String,
    text: String,
  )

  /** Input sub-structure of various calls */
  @ConfiguredJsonCodec case class HardSign(
    @JsonKey("time") gameTimeSet: Int,
    @JsonKey("datetime") start: DateTime,
    @JsonKey("endDatetime") end: DateTime,
    text: String,
  )

  /** Input sub-structure of room-overview */
  @ConfiguredJsonCodec case class RoomOverviewRoomOwner(
    username: String,
    badge: CustomBadge,
  )

  /** Input sub-structure of room-overview */
  @ConfiguredJsonCodec case class RoomOverviewStatPoint(
    value: Int,
    endTime: Int,
  )

  /**
   * Input sub-structure of room-overview.
   *
   * Each item in each list is a stat point, separated by the requested time interval.
   */
  @ConfiguredJsonCodec case class RoomOverviewSelectedStats(
    energyHarvested: List[RoomOverviewStatPoint],
    energyConstruction: List[RoomOverviewStatPoint],
    energyCreeps: List[RoomOverviewStatPoint],
    energyControl: List[RoomOverviewStatPoint],
    creepsProduced: List[RoomOverviewStatPoint],
    creepsLost: List[RoomOverviewStatPoint],
  )

  /**
   * Input sub-structure of room-overview.
   *
   * Each '*8', '*180' and '*1440' property is the total of a stat for the past hour, the past day,
   * and the past week respectively.
   */
  @ConfiguredJsonCodec case class RoomOverviewTotalStats(
    // TODO: find some way to do post-processing to turn this whole class into
    // a List<RoomOverviewStatPage> where RoomOverviewStatPage just has 'energy', 'energyConstruction',
    // etc. properties.
    energy8: Int,
    energy180: Int,
    energy1440: Int,
    energyConstruction8: Int,
    energyConstruction180: Int,
    energyConstruction1440: Int,
    energyControl8: Int,
    energyControl180: Int,
    energyControl1440: Int,
    energyCreeps8: Int,
    energyCreeps180: Int,
    energyCreeps1440: Int,
    creepsProduced8: Int,
    creepsProduced180: Int,
    creepsProduced1440: Int,
    creepsLost8: Int,
    creepsLost180: Int,
    creepsLost1440: Int,
  )

  @ConfiguredJsonCodec case class RoomOverview(
    owner: Option[RoomOverviewRoomOwner],
    stats: RoomOverviewSelectedStats,
    @JsonKey("statsMax") totals: RoomOverviewTotalStats,
  )
}
