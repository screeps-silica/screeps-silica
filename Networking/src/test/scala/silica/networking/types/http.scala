package silica.networking.types

import org.scalatest._
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import cats.syntax.functor._
import HttpDefs._
import silica.networking.types.util.Accept._
import silica.networking.types.util.EncodeEither._


class HttpSpec extends FlatSpec with Matchers {
  "MyInfo" should "parse example json" in {
    val json_string =
      """{
            "_id": "57874d42d0ae911e3bd15bbc",
            "badge": {
                "color1": "#260d0d",
                "color2": "#6b2e41",
                "color3": "#ffe56d",
                "flip": false,
                "param": -100,
                "type": 21
            },
            "cpu": 170,
            "credits": 0,
            "email": "daboross@daboross.net",
            "gcl": 571069296,
            "github": {
                "id": "1152146",
                "username": "daboross"
            },
            "lastRespawnDate": 1475270405700,
            "money": 3957697.9500000584,
            "notifyPrefs": {
                "errorsInterval": 0
            },
            "ok": 1,
            "password": true,
            "promoPeriodUntil": 1471635211172,
            "steam": {
                "displayName": "daboross",
                "id": "76561198033802814",
                "ownership": [
                    464350
                ]
            },
            "subscription": true,
            "subscriptionTokens": 0,
            "username": "daboross"
        }"""

    {
      val parsed = decode[Accept[MyInfo]](json_string)
      parsed should matchPattern { case Right(_) => }
    }

    {
      val parsed = decode[Accept[MyInfo]](json_string)
      parsed should matchPattern { case Right(Okay(_)) => }

      val asOkayInstance = decode[OkayInstance](parsed.right.get.okay.asJson.noSpaces)
      asOkayInstance should matchPattern { case Right(OkayInstance(1)) => }
    }
  }

  "LoginDetails" should "serialize into correct call" in {
    val details = LoginDetails("daboross", "pass1234")

    details.asJson.noSpaces shouldBe """{"email":"daboross","password":"pass1234"}"""
  }

  "LoggedIn" should "parse example json" in {
    val json = """{
      "ok": 1,
      "token": "c07924d3f556a355eba7cd59f4c21f670fda76c2"
    }"""

    {
      val parsed = decode[Accept[LoggedIn]](json)
      parsed should matchPattern { case Right(Okay(_)) => }
    }
  }

  }
}
