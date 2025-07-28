package elo

import game.DuelGame
import model.PlayerVillain
import tournaments.Tournament14

object PlayerVillainElo extends EloCalculable[DuelGame, PlayerVillain] {

  override def defaultElo(pv: PlayerVillain): Elo[PlayerVillain] = {
    Elo(1000.0, List.empty)
//    val bracket = Tournament14.brackets.indexWhere(_.contains(pv.villain))
//    val adjustment = if (bracket < 0) 0 else bracket - 2.5
//    Elo(1000.0 - 200 * adjustment, List.empty)
  }

  override def eloWinner(game: DuelGame): PlayerVillain = PlayerVillain(game.winnerPlayer, game.winner)

  override def eloLoser(game: DuelGame): PlayerVillain = PlayerVillain(game.loserPlayer, game.loser)

}
