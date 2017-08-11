import org.scalatest._
import util._
import Disposable.{With => With}
import scalaz.Scalaz

class TestDisposable extends FlatSpec with Matchers {

  it must "dispose of values upon reaching the end of a scope" in {
    var hasDisposed = false
    case class TestSubject(name: String)
    implicit object TestSubjectIsDisposable extends Disposable[TestSubject] {
      override def dispose(resource: TestSubject): Unit = { hasDisposed = true }
    }
    val subjName = "t"
    val subject = TestSubject(subjName)
    assert(!hasDisposed)
    With(subject)(subject => {
      assert(subject.name === subjName)
      assert(!hasDisposed)
    })
    assert(hasDisposed)
  }

  it must "call the work function before disposal" in {
    var hasDisposed = false
    var wasCalled = false
    case class TestSubject(name: String)
    implicit object TestSubjectIsDisposable extends Disposable[TestSubject] {
      override def dispose(resource: TestSubject): Unit = { hasDisposed = true }
    }
    val subjName = "t"
    val subject = TestSubject(subjName)
    assert(!hasDisposed)
    assert(!wasCalled)
    With(subject)(subject => {
      assert(subject.name === subjName)
      assert(!wasCalled)
      assert(!hasDisposed)
      wasCalled = true
    })
    assert(wasCalled)
    assert(hasDisposed)
  }
}
