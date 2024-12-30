package elo

import game.DuelGame
import model.PlayerVillain

object PlayerVillainElo extends EloCalculable[PlayerVillain] {

  override def eloWinner(game: DuelGame): PlayerVillain = PlayerVillain(game.winnerPlayer, game.winner)

  override def eloLoser(game: DuelGame): PlayerVillain = PlayerVillain(game.loserPlayer, game.loser)

}
