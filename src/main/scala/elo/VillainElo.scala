package elo

import game.DuelGame
import model.Villain

object VillainElo extends EloCalculable[Villain] {

  override def eloWinner(game: DuelGame): Villain = game.winner

  override def eloLoser(game: DuelGame): Villain = game.loser

}
