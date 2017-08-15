package silica.networking.types.util

import io.circe._
import io.circe.generic.extras._
import io.circe.syntax._

object Accept {

  sealed trait Accept[T] extends Any {
    final def okay: Okay[T] = this.asInstanceOf[Okay[T]]
    final def notAGoodLook: NotAGoodLook[T] = this.asInstanceOf[NotAGoodLook[T]]
  }

  private implicit val config: Configuration = Configuration.default.withDefaults

  final case class Okay[T](v: T) extends AnyVal with Accept[T]
  final case class NotAGoodLook[T](v: T) extends AnyVal with Accept[T]

  object Okay {
    implicit def encodeOkay[T: Encoder]: Encoder[Okay[T]] = {
      val tEncoder = implicitly[Encoder[T]]
      Encoder.instance[Okay[T]] {
        case Okay(t) => tEncoder(t).deepMerge(Accept.OkayInstance().asJson)
      }
    }
  }

  @ConfiguredJsonCodec case class OkayInstance(ok: Int = 1)

  implicit def decodeAccept[T](implicit tDecoder: Decoder[T]): Decoder[Accept[T]] = {
    Decoder[OkayInstance].and(tDecoder).map {
      case (OkayInstance(1), b) => Okay(b)
      case (_, b) => NotAGoodLook(b)
    }
  }

}
