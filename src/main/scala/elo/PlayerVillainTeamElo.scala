package elo

import game.TeamGame
import model.{PlayerVillain, Team}

object PlayerVillainTeamElo extends EloCalculable[TeamGame, Team[PlayerVillain]] {

  override def eloWinner(game: TeamGame): Team[PlayerVillain] = game.winner

  override def eloLoser(game: TeamGame): Team[PlayerVillain] = game.loser

}

