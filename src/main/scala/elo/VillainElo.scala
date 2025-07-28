package elo

import game.DuelGame
import model.Villain
import tournaments.Tournament14

object VillainElo extends EloCalculable[DuelGame, Villain] {

  override def defaultElo(v: Villain): Elo[Villain] = {
    Elo(1000.0, List.empty)
//    val bracket = Tournament14.brackets.indexWhere(_.contains(v))
//    val adjustment = if (bracket < 0) 0 else bracket - 2.5
//    Elo(1000.0 - 200 * adjustment, List.empty)
  }

  override def eloWinner(game: DuelGame): Villain = game.winner

  override def eloLoser(game: DuelGame): Villain = game.loser

}
