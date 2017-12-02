package scalaz.effect

import scala.concurrent.Await
import scala.concurrent.duration._

object FutureInterpreterExample {

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val rts = new DefaultRTS
    val iOInterpreter = FutureIOInterpreter(rts)

    val program = for {
      _ <- IO.sync(println("first"))
      _ <- IO.sync(println("second"))
      _ <- IO.sync(println("third"))
    } yield 3

    Await.ready(
      iOInterpreter.tryUnsafePerformIO(program).andThen {
        case r => println(s"iOInterpreter finished with a $r")
      },
      1.second
    )
    rts.shutdown()
    ()
  }

}
