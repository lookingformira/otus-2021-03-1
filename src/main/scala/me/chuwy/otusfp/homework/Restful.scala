package me.chuwy.otusfp.homework

import cats.data.Kleisli
import cats.effect.IO
import cats.effect.kernel.Ref

import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Request, Response}


object Restful {


  def allRoutes(ref: Ref[IO, Int]): Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "counter" => for {
      result <- ref.updateAndGet(_ + 1)
      response <- Ok(Counter(result))
    } yield response

    case GET -> Root / "slow" / IntVar(chunk) / IntVar(total) / IntVar(time) => Ok(ChunkReading.doChunkReading(chunk, total, time))
      .handleErrorWith(e => BadRequest(e.getMessage))
  }.orNotFound


}
