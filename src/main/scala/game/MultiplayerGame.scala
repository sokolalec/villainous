package model.game

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import model.{Player, Villain}

case class MultiplayerGame(winner: Villain,
                           winnerPlayer: Player,
                           losers: Map[Player, Villain],
                           playerOrder: List[Player],
                           date: Long)

object MultiplayerGame {

  implicit val losersDecoder: Decoder[Map[Player, Villain]] = Decoder.decodeMap[String, String].map { stringMap =>
    stringMap.map { case (playerName, villainName) => Player(playerName) -> Villain.withName(villainName) }
  }

  implicit val multiplayerGameDecoder: Long => Decoder[MultiplayerGame] = (epoch: Long) => new Decoder[MultiplayerGame] {
    final def apply(c: HCursor): Result[MultiplayerGame] = for {
      winner <- c.downField("winner").as[Villain]
      winnerPlayer <- c.downField("winnerPlayer").as[Player]
      losers <- c.downField("losers").as[Map[Player, Villain]]
      playerOrder <- c.downField("playerOrder").as[List[Player]]
    } yield MultiplayerGame(winner, winnerPlayer, losers, playerOrder, epoch)
  }

}
