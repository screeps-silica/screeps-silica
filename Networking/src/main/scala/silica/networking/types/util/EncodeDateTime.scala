package silica.networking.types.util

import io.circe.{Decoder, Encoder}
import org.joda.time.DateTime

object EncodeDateTime {

  implicit def encodeDateTime: Encoder[DateTime] =
    implicitly[Encoder[Long]].contramap(_.getMillis)

  implicit def decodeDateTime(implicit lDec: Decoder[Long], sDec: Decoder[String]): Decoder[DateTime] =
    lDec.or(sDec.map(_.toLong)).map(new DateTime(_))

}
