package asynchronous

import scala.concurrent._
import java.util.concurrent.Executors

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object FunctionalComposition {

  def main(args: Array[String]): Unit = {

    implicit val exc: ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

    fallBackTo
  }

  def fallBackTo()(implicit executionContext: ExecutionContext) = {
    val f = Future {throw new RuntimeException("error")}
    val g = Future {8}
    val h = g fallbackTo f
    Await.result(h, Duration.Zero)

    println(h)
  }

  def isPerfectSqaure(value: Int): Boolean = {
    if(value < 0)
      throw new IllegalArgumentException("Negative Number for Square root")
    else
      math.sqrt(value) % 1 == 0
  }

  def getPerfectSquare()(implicit executionContext: ExecutionContext) = {
    val future: Future[Seq[Int]] = Future {
      (1 to 100).filter(isPerfectSqaure)
    }

    val numbers: Future[String] = future.map{_.mkString(",")}

    numbers onComplete {
      case Success(x) => println(x)
      case Failure(e) => e.printStackTrace()
    }
  }
}
