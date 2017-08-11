package util

import scala.annotation.implicitNotFound
import scala.util.{Failure, Success, Try}


trait Disposable[T] {
  def dispose(resource: T): Unit
}


object Disposable {

  def disposableFor[T](t: T)(implicit disposable: Disposable[T]): Disposable[T] = disposable

  implicit object UnitIsDisposable extends Disposable[Unit] {
    def dispose(resource: Unit): Unit = {}
  }

  implicit object NoneIsDisposable extends Disposable[None.type] {
    def dispose(resource: None.type): Unit = {}
  }

  implicit def SomeIsDisposable[T : Disposable]: Disposable[Some[T]] = {
    (resource: Some[T]) => implicitly[Disposable[T]].dispose(resource.get)
  }

  implicit def OptionIsDisposable[T : Disposable]: Disposable[Option[T]] = {
    (resource: Option[T]) => {
      resource match {
        case Some(value) => implicitly[Disposable[T]].dispose(value)
        case _ => None
      }
    }
  }

  import java.lang.AutoCloseable
  implicit object AutoCloseableIsDisposable extends Disposable[AutoCloseable] {
    override def dispose(resource: AutoCloseable): Unit = resource.close()
  }

  import java.io.Closeable
  implicit object CloseableIsDisposable extends Disposable[Closeable] {
    override def dispose(resource: Closeable): Unit = resource.close()
  }

  @implicitNotFound("Resource does not belong to typeclass Disposable")
  final def With[TRes : Disposable, B](resource: TRes)(doWork: TRes => B): Try[B] = {
    try {
      Success(doWork(resource))
    } catch {
      case e: Exception => Failure(e)
    } finally {
      implicitly[Disposable[TRes]].dispose(resource)
    }
  }

}
