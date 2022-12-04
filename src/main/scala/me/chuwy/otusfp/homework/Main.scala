package me.chuwy.otusfp.homework

import cats.effect.kernel.Ref
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  val counter: IO[Ref[IO, Int]] = Ref.of[IO, Int](0)

  override def run(args: List[String]): IO[ExitCode] = for {
    ref <- counter
    r <- BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(Restful.allRoutes(ref))
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  } yield r
}
