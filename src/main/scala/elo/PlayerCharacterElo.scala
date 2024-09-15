package elo

import model.PlayerVillain
import model.game.DuelGame

object PlayerCharacterElo extends EloCalculable[PlayerVillain] {

  override def eloWinner(game: DuelGame): PlayerVillain = PlayerVillain(game.winnerPlayer, game.winner)

  override def eloLoser(game: DuelGame): PlayerVillain = PlayerVillain(game.loserPlayer, game.loser)

}
