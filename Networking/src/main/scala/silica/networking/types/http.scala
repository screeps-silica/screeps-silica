package silica.networking.types

import cats.syntax.functor._
import io.circe._
import io.circe.generic.semiauto._
import io.circe.generic.extras._
import io.circe.syntax._

object HttpDefs {


  sealed trait Accept[T] extends Any {
    final def okay: Okay[T] = this.asInstanceOf[Okay[T]]
    final def notAGoodLook: NotAGoodLook[T] = this.asInstanceOf[NotAGoodLook[T]]
  }
  final case class Okay[T](v: T) extends AnyVal with Accept[T]
  final case class NotAGoodLook[T](v: T) extends AnyVal with Accept[T]

  @ConfiguredJsonCodec case class OkayInstance(ok: Int = 1)

  implicit def encodeAccept[T: Encoder]: Encoder[Okay[T]] = {
    val tEncoder = implicitly[Encoder[T]]
    Encoder.instance[Okay[T]] {
      case Okay(t) => tEncoder(t).deepMerge(OkayInstance().asJson)
    }
  }

  implicit def decodeAccept[T](implicit tDecoder: Decoder[T]): Decoder[Accept[T]] = {
    Decoder[OkayInstance].and(tDecoder).map {
      case (OkayInstance(1), b) => Okay(b)
      case (_, b) => NotAGoodLook(b)
    }
  }

  implicit def encodeEither[B: Encoder, C: Encoder]: Encoder[Either[B, C]] = {
    val bEncoder = implicitly[Encoder[B]]
    val cEncoder = implicitly[Encoder[C]]
    Encoder.instance[Either[B, C]] {
      case Left(b) => bEncoder(b)
      case Right(c) => cEncoder(c)
    }
  }

  implicit def decodeEither[B: Decoder, C: Decoder]: Decoder[Either[B, C]] = {
    val bDecoder = implicitly[Decoder[B]]
    val cDecoder = implicitly[Decoder[C]]
    bDecoder.map(Left(_)).or(cDecoder.map(Right(_)))
  }

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
    steam: SteamInfo,
    subscription: Boolean,
    subscriptionTokens: Int,
  )

}
