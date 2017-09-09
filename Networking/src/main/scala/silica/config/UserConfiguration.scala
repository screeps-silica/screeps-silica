package silica.config

import java.nio.charset.Charset
import java.nio.file._

import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}
import io.circe.parser._


object UserConfiguration {

  implicit val config: Configuration = Configuration.default.withDefaults

  @ConfiguredJsonCodec case class UserConfig(
    username: String,
    email: String,
    password: String,
    ptr: Boolean,
  )

  def loadFile(): UserConfig = {
    val path = Paths.get("config.json")

    val data = new String(Files.readAllBytes(path), Charset.forName("utf-8"))


    decode[UserConfig](data) match {
      case Right(user_config) => user_config
      case Left(err) => throw new Error("configuration loading failed", err)
    }
  }
}
