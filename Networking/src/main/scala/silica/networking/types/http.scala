package silica.networking.types

import io.circe.generic.extras._

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

}
