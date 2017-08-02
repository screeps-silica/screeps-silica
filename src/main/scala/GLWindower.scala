import java.util.Date

import util._
import org.lwjgl
import org.lwjgl.glfw._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL

import scalaz.Scalaz._
import scala.concurrent.duration._
import scala.language.implicitConversions

case class GLFWWindowRef(private val handle: Long) {
  final def title_=(title: String): Unit = glfwSetWindowTitle(handle, title)
  def shouldClose_=(shouldClose: Boolean): Unit = glfwSetWindowShouldClose(handle, shouldClose)
  def keyCallback_=(glfwKeyCallback: GLFWKeyCallback): Unit = glfwSetKeyCallback(handle, glfwKeyCallback)
}

object GLFWWindowRef {
  def create(handle: Long): Option[GLFWWindowRef] = handle match {
    case 0L => None
    case _ => Some(new GLFWWindowRef(handle))
  }

  implicit class GLWindowRefImplicits(winRef: GLFWWindowRef) extends AnyRef {
    def asHandle: Long = winRef.handle
  }

  implicit def asHandle(glfwWindowRef: GLFWWindowRef): Long = glfwWindowRef.handle
}

class GLWindower() {

  private val errorCallback: GLFWErrorCallback = GLFWErrorCallback.createThrow
  glfwSetErrorCallback(errorCallback)

  if(!glfwInit()) {
    throw new IllegalStateException("Unable to initialize GLFW")
  }

  private val window = GLFWWindowRef.create(glfwCreateWindow(1024, 768, "Silica", 0, 0)) match {
    case Some(h) => h
    case None =>
      glfwTerminate()
      throw new IllegalStateException("Unable to create GLFW window")
  }

  private val keyCallback = new GLFWKeyCallback() {
    override def invoke(windowHandle: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
      val win = GLFWWindowRef.create(windowHandle).getOrElse({
        glfwTerminate()
        throw new IllegalStateException("Window handle invalid")
      })
      if (key === GLFW_KEY_ESCAPE && action === GLFW_PRESS) {
        win.shouldClose_=(true)
      }
    }
  }

  window.keyCallback_=(this.keyCallback)


  glfwMakeContextCurrent(window)
  GL.createCapabilities()

  private def processInput(time: Double): Unit = {
    glfwPollEvents()
  }

  private def render(time: Double): Unit = {
    glfwSwapBuffers(window)
  }

  def runLoop(): Unit = {
    val sleepMillis = 16.6.milliseconds.toMillis
    while (!glfwWindowShouldClose(window)) {
      val time = glfwGetTime()
      processInput(time)
      render(time)
      Thread.sleep(sleepMillis)
    }
    glfwDestroyWindow(window)
    keyCallback.free()
    glfwTerminate()
    errorCallback.free()
  }
}

object GLWindower {
  def apply(): GLWindower = new GLWindower()
}

