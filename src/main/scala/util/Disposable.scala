package util

import scala.annotation.implicitNotFound
import scala.util.{Failure, Success, Try}
import java.io.Closeable


trait Disposable[T] extends Any {
  def dispose(resource: T): Unit
}

object Disposable {

  final def disposableFor[T](t: => T)(implicit disposable: Disposable[T]): Disposable[T] = disposable

  implicit object UnitIsDisposable extends Disposable[Unit] {
    def dispose(resource: Unit): Unit = {}
  }

  implicit object NoneIsDisposable extends Disposable[None.type] {
    def dispose(resource: None.type): Unit = {}
  }

  final implicit def SomeIsDisposable[T: Disposable]: Disposable[Some[T]] =
    (resource: Some[T]) => implicitly[Disposable[T]].dispose(resource.get)

  final implicit def OptionIsDisposable[T: Disposable]: Disposable[Option[T]] =
    (resource: Option[T]) => resource.foreach(value => implicitly[Disposable[T]].dispose(value))

  implicit object AutoCloseableIsDisposable extends Disposable[AutoCloseable] {
    override def dispose(resource: AutoCloseable): Unit = resource.close()
  }

  implicit object CloseableIsDisposable extends Disposable[Closeable] {
    override def dispose(resource: Closeable): Unit = resource.close()
  }

  @implicitNotFound("Resource does not belong to typeclass Disposable")
  final def With[TRes: Disposable, B](resource: TRes)(doWork: TRes => B): Try[B] = {
    try {
      Success(doWork(resource))
    } catch {
      case e: Exception => Failure(e)
    } finally {
      implicitly[Disposable[TRes]].dispose(resource)
    }
  }

  final def Func[T](callback: => Any)(block: => T): Try[T] = {
    try {
      Success(block)
    } catch {
      case e: Exception => Failure(e)
    } finally {
      callback
    }
  }

  class LambdaDisposable[T](val item: T, val disposer: T => Any) {
    final def dispose(): Unit = disposer(item)
  }

  final implicit def lambdaDisposableIsDisposable[T]: Disposable[LambdaDisposable[T]] =
    (resource: LambdaDisposable[T]) => resource.dispose()

  final def Create[T](resource: T)(disposer: T => Any): LambdaDisposable[T] = new LambdaDisposable(resource, disposer)

}
