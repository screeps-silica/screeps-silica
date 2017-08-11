import java.util.Date

import util._
import org.lwjgl
import org.lwjgl.glfw._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl
import opengl.GL33._
import opengl.GL32._
import opengl.GL31._
import opengl.GL30._

import scalaz.Scalaz._
import scala.concurrent.duration._
import scala.language.implicitConversions
import Disposable.{Func, With}

import scala.annotation.tailrec

case class GLVertexArray(handle: Int) extends AnyVal
object GLVertexArray {
  implicit object GLVertexArrayIsDisposable extends Disposable[GLVertexArray] {
    override def dispose(vertArray: GLVertexArray): Unit = glDeleteVertexArrays(vertArray.handle)
  }
}

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

  if (!glfwInit()) {
    throw new IllegalStateException("Unable to initialize GLFW")
  }

  glfwDefaultWindowHints()
  glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
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
      (key, action) match {
        case (GLFW_KEY_ESCAPE, GLFW_PRESS) => win.shouldClose_=(true)
        case _ => Unit
      }
    }
  }

  window.keyCallback_=(this.keyCallback)


  glfwMakeContextCurrent(window)
  lwjgl.opengl.GL.createCapabilities()

  private def processInput(time: Double, deltaTime: Double): Unit = {
    glfwPollEvents()
  }

  private def render(time: Double, deltaTime: Double): Unit = {
    glfwSwapBuffers(window)
    With(GLVertexArray(glGenVertexArrays()))(vao => glBindVertexArray(vao.handle))
  }

  private implicit object GLFWWindowRefIsDisposable extends Disposable[GLFWWindowRef] {
    override def dispose(window: GLFWWindowRef): Unit = glfwDestroyWindow(window)
  }

  private implicit object KeyCallbackIsDisposable extends Disposable[GLFWKeyCallback] {
    override def dispose(callback: GLFWKeyCallback): Unit = callback.free()
  }

  private implicit object ErrorCallbackIsDisposable extends Disposable[GLFWErrorCallback] {
    override def dispose(callback: GLFWErrorCallback): Unit = callback.free()
  }

  def runLoop(): Unit = {
    val sleepMillis = 16.6.milliseconds.toMillis

    @tailrec
    def loop(lastTime: Double, sleepMillis: Long): Unit = {
      if (!glfwWindowShouldClose(window)) {
        val time = glfwGetTime()
        val deltaTime = time - lastTime
        processInput(time, deltaTime)
        render(time, deltaTime)
        Thread.sleep(sleepMillis)
        loop(time, sleepMillis)
      }
    }

    With(errorCallback)(_ =>
      With(Disposable.Create(Unit)(_ => glfwTerminate()))(_ => {
        With(keyCallback)(_ =>
          With(window)(_ => loop(glfwGetTime(), sleepMillis))
        )
      })
    )
  }
}

object GLWindower {
  def apply(): GLWindower = new GLWindower()
}

