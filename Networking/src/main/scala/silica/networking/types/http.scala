package silica.networking.types

import io.circe._, io.circe.generic.semiauto._

object HttpDefs {
    case class CustomBadge(
        path1: String,
        path2: String
    )

    implicit val customBadgeDecoder: Decoder[CustomBadge] = deriveDecoder

    implicit val eitherDecode: Decoder[Either[Int, CustomBadge]] = deriveDecoder

    case class MyInfo(
        ok: Int,
        _id: String,
        username: String,
        password: Boolean,
        cpu: Int,
        gcl: Long,
        money: Double,
        badge: Either[Int, CustomBadge]
    )

    implicit val myInfoDecoder: Decoder[MyInfo] = deriveDecoder

}
