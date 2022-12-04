package me.chuwy.otusfp.homework

import cats.effect._
import cats.effect.kernel.Ref
import cats.effect.testing.specs2.CatsEffect
import io.circe.Json
import io.circe.parser._
import org.http4s._
import org.http4s.implicits._
import org.specs2.mutable.Specification


class RestfulTest extends Specification with CatsEffect {

  val counter: IO[Ref[IO, Int]] = Ref.of[IO, Int](0)


  "Counter" should {
    "increment" in {

      for {
        ref <- counter
        _ <- Restful.allRoutes(ref).run(
          Request(Method.GET, uri = uri"/counter")
        )
        response <- Restful.allRoutes(ref).run(
          Request(Method.GET, uri = uri"/counter")
        )
        _ <- response.bodyText.compile.last.flatMap(body => IO.println(parse(body.get).getOrElse(Json.Null).as[Counter]))
        result <- response.bodyText.compile.last.map(body => parse(body.get).getOrElse(Json.Null).as[Counter])
      } yield (result.getOrElse(Counter(-1)) must beEqualTo(Counter(2))) and (response.status.isSuccess must beEqualTo(true))

    }
  }

  "Slow" should {
    "work correct" in {

      for {
        ref <- counter
        response <- Restful.allRoutes(ref).run(
          Request(Method.GET, uri = uri"/slow/5/10/1")
        )
        _ <- response.bodyText.compile.last.flatMap(body => IO.println(parse(body.get).getOrElse(Json.Null).as[String]))
        result <- response.bodyText.compile.last.map(body => parse(body.get).getOrElse(Json.Null).as[String])
      } yield (result.getOrElse("") must beEqualTo("f" * 10)) and (response.status.isSuccess must beEqualTo(true))

    }
  }


}

