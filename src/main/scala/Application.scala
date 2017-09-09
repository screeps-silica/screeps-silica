import util._

object Application {
  def main(args: Array[String]): Unit = {
//    println(System.getProperty("java.class.path"))
    println(s"LWJGL Version: ${org.lwjgl.Version.getVersion}")

    val window = GLWindower()
    window.runLoop()

  }
}
