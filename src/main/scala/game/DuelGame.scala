package game

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import model.{Player, Villain}
import util.Datetime.epochOf

case class DuelGame(winner: Villain,
                    loser: Villain,
                    winnerPlayer: Player,
                    loserPlayer: Player,
                    firstPlayer: Villain,
                    date: Long,
                    tournament: Boolean) {
  val isLegal: Boolean = winner.playedCorrectSince < date && loser.playedCorrectSince < date

  override def toString: String =
    s"$date Game(w: $winner, l: $loser, wp: $winnerPlayer, lp: $loserPlayer, fp: $firstPlayer)"
}

object DuelGame {

  implicit val duelGameOrdering: Ordering[DuelGame] = Ordering.by(_.date)

  implicit val otherGameDecoder: Decoder[DuelGame] = new Decoder[DuelGame] {
    final def apply(c: HCursor): Result[DuelGame] = for {
      date <- c.downField("date").as[String]
      winner <- c.downField("winner").as[Villain]
      loser <- c.downField("loser").as[Villain]
      winnerPlayer <- c.downField("winnerPlayer").as[Player]
      loserPlayer <- c.downField("loserPlayer").as[Player]
      firstPlayer <- c.downField("firstPlayer").as[Villain]
    } yield DuelGame(winner, loser, winnerPlayer, loserPlayer, firstPlayer, epochOf(date), tournament = false)
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
