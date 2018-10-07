package asynchronous

import scala.concurrent._
import scala.util.{Failure, Success}
import java.util.concurrent.Executors


object NonBlockingTest {

  def main(args: Array[String]): Unit = {

    implicit val exc: ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

    val f = square(100000)
    println(f)
  }

  /**
    * Asynchronous and non-blocking
    * @param n
    * @param executionContext
    * @return
    */
  def square(n: Int)(implicit executionContext: ExecutionContext): Future[Double] = Future {
    math.sqrt(n)
  }

}
