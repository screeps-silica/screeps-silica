package silica.config

object Configuration {
  import io.circe.Encoder, io.circe.Json

  import io.circe.generic.extras._
  import io.circe.syntax._
  import java.nio.file._
  import java.nio.charset.Charset

  import io.circe._
  import io.circe.parser._


  implicit val config: Configuration = Configuration.default.withDefaults

  @ConfiguredJsonCodec case class UserConfig(
    username: String,
    email: String,
    password: String,
    ptr: Boolean,
  )

  def loadFile(): UserConfig = {
    val path = Paths.get("config.json")

    val data = Files.readAllBytes(path)

    decode[UserConfig](data.asJson)
  }
}
