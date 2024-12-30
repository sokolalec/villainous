package elo

import game.DuelGame
import model.PlayerTeam

object PlayerTeamElo extends EloCalculable[PlayerTeam] {

  override def eloWinner(game: DuelGame): PlayerTeam = PlayerTeam(game.winnerPlayer, Set(game.winner))

  override def eloLoser(game: DuelGame): PlayerTeam = PlayerTeam(game.loserPlayer, Set(game.loser))

}
