package silica.networking.types.util

import io.circe._

object EncodeEither {

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

}
