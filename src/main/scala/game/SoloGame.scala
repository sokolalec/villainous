package model.game

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import model.{Player, Villain}

case class SoloGame(winner: Villain, loser: Villain, player: Player, firstPlayer: Villain, date: Long) {
  def toDuelGame() = DuelGame(winner, loser, player, player, firstPlayer, date, tournament = false)
}

object SoloGame {

  implicit val soloGameDecoder: Long => Decoder[SoloGame] = (epoch: Long) => new Decoder[SoloGame] {
    final def apply(c: HCursor): Result[SoloGame] = for {
      winner <- c.downField("winner").as[Villain]
      loser <- c.downField("loser").as[Villain]
      player <- c.downField("player").as[Player]
      firstPlayer <- c.downField("firstPlayer").as[Villain]
    } yield SoloGame(winner, loser, player, firstPlayer, epoch)
  }

}
