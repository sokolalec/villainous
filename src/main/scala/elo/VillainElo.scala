package elo

import model.Villain
import model.game.DuelGame

object VillainElo extends EloCalculable[Villain] {

  override def eloWinner(game: DuelGame): Villain = game.winner

  override def eloLoser(game: DuelGame): Villain = game.loser

}
