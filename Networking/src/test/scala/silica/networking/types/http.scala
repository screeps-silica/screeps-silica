package silica.networking.types

import org.scalatest._
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import cats.syntax.functor._
import HttpDefs._
import org.joda.time.DateTime
import silica.networking.types.util.Accept._
import silica.networking.types.util.EncodeDateTime._
import silica.networking.types.util.EncodeEither._


class HttpSpec extends FunSpec with Matchers {

  describe("API types") {
    describe("MyInfo") {
      it("should parse example json") {
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
    }

    describe("LoginDetails") {
      it("should serialize into correct call") {
        val details = LoginDetails("daboross", "pass1234")
        details.asJson.noSpaces shouldBe """{"email":"daboross","password":"pass1234"}"""
      }
    }

    describe("LoggedIn") {
      it("should parse example json") {
        val json =
          """{
         "ok": 1,
          "token": "c07924d3f556a355eba7cd59f4c21f670fda76c2"
        }"""

        {
          val parsed = decode[Accept[LoggedIn]](json)
          parsed should matchPattern { case Right(Okay(_)) => }
        }
      }
    }

    describe("MapStatName.RoomOwner") {
      it("should serialize into correct string") {
        val stat: MapStatName = MapStatName.RoomOwner
        stat.asJson.noSpaces shouldBe """"owner0""""
      }
    }

    describe("MapStatName.Claim") {
      it("should serialize into correct string") {
        val stat: MapStatName = MapStatName.Claim
        stat.asJson.noSpaces shouldBe """"claim0""""
      }
    }

    describe("MapStatsDetails") {
      it("should serialize into correct string") {
        val details = MapStatsDetails(List("E4S61", "E5N70"), MapStatName.RoomOwner, "shard0")

        details.asJson.noSpaces shouldBe """{"rooms":["E4S61","E5N70"],"stat":"owner0","shard":"shard0"}"""
      }
    }

    describe("MapStats") {
      it("should parse example json") {
        val json =
          """{
      "ok": 1,
      "stats": {
        "E14S78": {
          "own": {
            "level": 0,
            "user": "57fbb4ada59532b2194a4c4e"
          },
          "sign": {
            "time": 18325590,
            "text": "[Ypsilon Pact] Quad claimed: unauthorised rooms may be removed.",
            "user": "57fbb4ada59532b2194a4c4e",
            "datetime": 1490752580310
          },
          "status": "normal",
          "novice": 1485278202869
        },
        "E15N52": {
          "own": {
            "level": 8,
            "user": "57874d42d0ae911e3bd15bbc"
          },
          "openTime": "1474674699273",
          "status": "normal",
          "novice": 1475538699273
        },
        "E19S81": {
          "status": "normal",
          "novice": 1491937635414
        },
        "E19S79": {
          "own": {
            "level": 3,
            "user": "57e0dde6adafdf710cc02af0"
          },
          "sign": {
            "time": 18318966,
            "text": "Outer reach settlement",
            "user": "57e0dde6adafdf710cc02af0",
            "datetime": 1490723256463
          },
          "status": "normal",
          "novice": 1485278202869,
          "safeMode": true
        },
        "W6S67": {
          "sign": {
            "time": 16656131,
            "text": "I have plans for this block",
            "user": "57c7df771d90a0c561977377",
            "datetime": 1484071532985
          },
          "status": "normal",
          "novice": 1482080519526,
          "hardSign": {
            "time": 18297994,
            "endDatetime": 1490978122587,
            "text": "A new Novice Area is being planned somewhere in this sector. Please make sure all important rooms are reserved.",
            "datetime": 1490632558393
          }
        }
      },
      "gameTime": 18325591,
      "users": {
        "57e0dde6adafdf710cc02af0": {
          "username": "Pav234",
          "_id": "57e0dde6adafdf710cc02af0",
          "badge": {
            "color1": "#25009c",
            "flip": false,
            "param": 100,
            "color3": "#00c7ff",
            "type": 16,
            "color2": "#00c7ff"
          }
        },
        "57fbb4ada59532b2194a4c4e": {
          "username": "Parthon",
          "_id": "57fbb4ada59532b2194a4c4e",
          "badge": {
            "color1": "#0066ff",
            "flip": false,
            "param": -6,
            "color3": "#2b2b2b",
            "type": 16,
            "color2": "#00ddff"
          }
        },
        "57c7df771d90a0c561977377": {
          "username": "ChaosDMG",
          "_id": "57c7df771d90a0c561977377",
          "badge": {
            "color1": "#f25c00",
            "flip": false,
            "param": 0,
            "color3": "#f7efe2",
            "type": 17,
            "color2": "#f9a603"
          }
        },
        "57874d42d0ae911e3bd15bbc": {
          "username": "daboross",
          "_id": "57874d42d0ae911e3bd15bbc",
          "badge": {
            "color1": "#260d0d",
            "flip": false,
            "param": -100,
            "color3": "#ffe56d",
            "type": 21,
            "color2": "#6b2e41"
          }
        }
      }
    }"""

        {
          val parsed = decode[Accept[MapStats]](json)
          parsed should matchPattern { case Right(Okay(_)) => }
        }
      }
    }
  }

  describe("Time Encoding") {
    it("must encode reversibly") {
      val startTime = DateTime.now()
      assert(startTime.getMillis === decode[DateTime](startTime.asJson.noSpaces).right.get.getMillis)
    }

    it("must map linearly as a function of time") {
      def roundTrip(dateTime: DateTime): DateTime = decode[DateTime](dateTime.asJson.noSpaces).right.get
      val now = DateTime.now()

      roundTrip(now).plusSeconds(1) shouldEqual roundTrip(now.plusSeconds(1))
      roundTrip(now).minusDays(1) shouldEqual roundTrip(now.minusDays(1))
    }
  }
}
