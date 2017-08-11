package silica.networking.types


import argonaut._, Argonaut._


object HttpDefs {
    case class CustomBadge(
        path1: String,
        path2: String
    )

    implicit def CustomBadgeJson: CodecJson[CustomBadge] =
        casecodec2(CustomBadge.apply, CustomBadge.unapply)("path1", "path2")

    case class MyInfo(
        ok: Int,
        id: String,
        username: String,
        password: Boolean,
        cpu: Int,
        gcl: Long,
        money: Double,
        badge: Either[Int, CustomBadge]
    )

    implicit def MyInfoJson: CodecJson[MyInfo] =
        casecodec8(MyInfo.apply, MyInfo.unapply)(
            "ok",
            "_id",
            "username",
            "password",
            "cpu",
            "gcl",
            "money",
            "badge"
        )
}
