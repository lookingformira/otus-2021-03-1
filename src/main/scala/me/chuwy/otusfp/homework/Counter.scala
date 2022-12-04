package me.chuwy.otusfp.homework

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf


case class Counter(counter: Int)

object Counter {
  implicit val carsEncoder: Encoder[Counter] = deriveEncoder[Counter]
  implicit val carsDecoder: Decoder[Counter] = deriveDecoder[Counter]
  implicit val decoder: EntityDecoder[IO, Counter] = jsonOf[IO, Counter]
}

