package model.game

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import model.environment.{Player, Villain}
import util.Datetime.epochOf

case class DuelGame(winner: Villain,
                    loser: Villain,
                    winnerPlayer: Player,
                    loserPlayer: Player,
                    firstPlayer: Villain,
                    date: Long,
                    tournament: Boolean) {
  override def toString: String =
    s"Game(w: $winner, l: $loser, wp: $winnerPlayer, lp: $loserPlayer, fp: $firstPlayer)"
}

object DuelGame {

  implicit val otherGameDecoder: Long => Decoder[DuelGame] = (epoch: Long) => new Decoder[DuelGame] {
    final def apply(c: HCursor): Result[DuelGame] = for {
      winner <- c.downField("winner").as[Villain]
      loser <- c.downField("loser").as[Villain]
      winnerPlayer <- c.downField("winnerPlayer").as[Player]
      loserPlayer <- c.downField("loserPlayer").as[Player]
      firstPlayer <- c.downField("firstPlayer").as[Villain]
    } yield DuelGame(winner, loser, winnerPlayer, loserPlayer, firstPlayer, epoch, tournament = false)
  }

  implicit val tournamentGameDecoder: Decoder[DuelGame] = new Decoder[DuelGame] {
    final def apply(c: HCursor): Result[DuelGame] = for {
      date <- c.downField("date").as[String]
      winner <- c.downField("winner").as[Villain]
      loser <- c.downField("loser").as[Villain]
      winnerPlayer <- c.downField("winnerPlayer").as[Player]
      loserPlayer <- c.downField("loserPlayer").as[Player]
      firstPlayer <- c.downField("firstPlayer").as[Villain]
    } yield DuelGame(winner, loser, winnerPlayer, loserPlayer, firstPlayer, epochOf(date), tournament = true)
  }

}
