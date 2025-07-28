package game

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import model.{Player, Villain}
import util.Datetime.epochOf

case class SoloGame(winner: Villain, loser: Villain, player: Player, firstPlayer: Villain, date: Long) {
  def toDuelGame: DuelGame = DuelGame(winner, loser, player, player, firstPlayer, date, tournament = false)
}

object SoloGame {

  implicit val soloGameOrdering: Ordering[SoloGame] = Ordering.by(_.date)

  implicit val soloGameDecoder: Decoder[SoloGame] = new Decoder[SoloGame] {
    final def apply(c: HCursor): Result[SoloGame] = for {
      date <- c.downField("date").as[String]
      winner <- c.downField("winner").as[Villain]
      loser <- c.downField("loser").as[Villain]
      player <- c.downField("player").as[Player]
      firstPlayer <- c.downField("firstPlayer").as[Villain]
    } yield SoloGame(winner, loser, player, firstPlayer, epochOf(date))
  }

}
