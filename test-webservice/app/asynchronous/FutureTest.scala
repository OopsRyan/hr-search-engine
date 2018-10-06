package asynchronous


import java.util.concurrent.Executors

import scala.util.{Failure, Success}
import scala.concurrent.{Await, ExecutionContext, Future}
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object FutureTest {

  def main(args: Array[String]): Unit = {
//    val future: Future[Double] = Future {
//      math.sqrt(100000)
//    }

//    synchronousTest
//    asynchronousTest

    implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

    threadPoolTest(ec)

    println("Outside")
  }

  def synchronousTest()(implicit executionContext: ExecutionContext) = {
    println("Before future")

    val future: Future[Double] = Future {
      Thread.sleep(5000)

      println("Inside future")

      math.sqrt(100000)
    }

    println("Outside future")

    val ans: Double = Await.result(future, 6 seconds)

    println(ans)
  }

  def asynchronousTest()(implicit executionContext: ExecutionContext) = {
    val future: Future[Double] = Future { math.sqrt(100000) }

    future onComplete {
      case Success(x) => println("Success: " + x);
      case Failure(e) => e.printStackTrace();
    }

    Thread.sleep(1000)
  }

  def threadPoolTest(implicit ec: ExecutionContext) = {

    def search(word: String, text: Array[String])(implicit ec: ExecutionContext)
    : Future[Array[String]] = Future {
      text.filter(_ contains word)
    }

    val text = Array("aaahe", "haeehea", "aaa", "bb")
    val ans: Future[Array[String]] = search("he", text)

    ans onComplete {
      case Success(x) => x.foreach(println)
      case Failure(e) => println(s"Failure : ${e.getMessage}")
    }

    ans onSuccess {
      case x: Array[String] if x.length > 0 => println("Yah. Found")
      case _ => println("Oops")
    }

    ans onFailure {
      case ex: Exception => ex.printStackTrace()
    }
  }
}
