package me.chuwy.otusfp.homework

import cats.effect.IO
import fs2.Stream

import scala.annotation.tailrec
import scala.concurrent.duration.DurationInt

object ChunkReading {

  private def makeChunk(size: Int): String = {
    @tailrec
    def loop(message: String): String = {
      if (message.getBytes.length >= size) message else loop(message + "f")
    }

    loop("f")
  }

  def doChunkReading(chunkSize: Int, totalSize: Int, time: Int): Stream[IO, String] = Stream.unfoldEval("") { s =>
    val next = s + makeChunk(chunkSize)
    IO.sleep(time.second) *> {
      if (s.getBytes.length >= totalSize) IO.none
      else IO(next).as(Some((next, next)))
    }
  }

}
