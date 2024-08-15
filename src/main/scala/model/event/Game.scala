package model.event

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder, HCursor}
import model.Player
import model.environment.Villain
import util.Datetime.epochOf

case class Game(winner: Villain,
                loser: Villain,
                winnerPlayer: Player,
                loserPlayer: Player,
                firstPlayer: Villain,
                date: Long,
                tournament: Boolean) {
  override def toString: String =
    s"Game(w: $winner, l: $loser, wp: $winnerPlayer, lp: $loserPlayer, fp: $firstPlayer)"
}

object Game {

  implicit val playerDecoder: Decoder[Player] = new Decoder[Player] {
    final def apply(c: HCursor): Decoder.Result[Player] = c.as[String].map(Player(_))
  }

  implicit val otherGameDecoder: Long => Decoder[Game] = (epoch: Long) => new Decoder[Game] {
    final def apply(c: HCursor): Decoder.Result[Game] = for {
      winner <- c.downField("winner").as[Villain]
      loser <- c.downField("loser").as[Villain]
      winnerPlayer <- c.downField("winnerPlayer").as[Player]
      loserPlayer <- c.downField("loserPlayer").as[Player]
      firstPlayer <- c.downField("firstPlayer").as[Villain]
    } yield Game(winner, loser, winnerPlayer, loserPlayer, firstPlayer, epoch, false)
  }

  implicit val tournamentGameDecoder: Decoder[Game] = new Decoder[Game] {
    final def apply(c: HCursor): Decoder.Result[Game] = for {
      date <- c.downField("date").as[String]
      winner <- c.downField("winner").as[Villain]
      loser <- c.downField("loser").as[Villain]
      winnerPlayer <- c.downField("winnerPlayer").as[Player]
      loserPlayer <- c.downField("loserPlayer").as[Player]
      firstPlayer <- c.downField("firstPlayer").as[Villain]
    } yield Game(winner, loser, winnerPlayer, loserPlayer, firstPlayer, epochOf(date), true)
  }

}
