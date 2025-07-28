package elo

import game.TeamGame
import model.{Team, Villain}

object VillainTeamElo extends EloCalculable[TeamGame, Team[Villain]] {

  override def eloWinner(game: TeamGame): Team[Villain] = Team(game.winner.members.map(_.villain))

  override def eloLoser(game: TeamGame): Team[Villain] = Team(game.loser.members.map(_.villain))
  
}
