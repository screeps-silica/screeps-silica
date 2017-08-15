package silica.networking.types

import io.circe.generic.extras._

object HttpDefs {
  import silica.networking.types.util.EncodeEither._

  implicit val config: Configuration = Configuration.default.withDefaults

  @ConfiguredJsonCodec case class CustomBadge(
    color1: String,
    color2: String,
    color3: String,
    flip: Boolean,
    param: Int,
    @JsonKey("type") tpe: Int
  )

  @ConfiguredJsonCodec case class NotifyPrefs(
    errorsInterval: Int,
  )

  @ConfiguredJsonCodec case class SteamInfo(
    displayName: String,
    id: String,
    ownership: List[Int],
  )

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

}
