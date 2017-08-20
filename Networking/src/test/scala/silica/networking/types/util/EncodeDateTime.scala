package silica.networking.types.util

import io.circe.{Decoder, Encoder}
import org.joda.time.DateTime

import org.scalatest._
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import cats.syntax.functor._
import silica.networking.types.util.Accept._
import silica.networking.types.util.EncodeDateTime._
import silica.networking.types.util.EncodeEither._



class DateTimeSpec extends FunSpec with Matchers {
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

    it("must decode string times") {
        val json = "\"1234\""

        assert(1234 === decode[DateTime](json).right.get.getMillis)
    }

    it("must decode integer times") {
        val json = "1234"

        assert(1234 === decode[DateTime](json).right.get.getMillis)
    }
  }
}
