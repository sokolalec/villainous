package util

import model.environment.Expansion.ownedExpansions
import model.environment.{MatchUp, Villain}
import model.environment.Villain.{Gaston, LadyTremaine, MadamMim}
import model.event.{Game, PlayableGame}
import model.{Player, event}
import util.Filesystem.{readJsonFile, tournamentFiles}
import util.MatchUps.getMatch
import util.RNG.getRandom

import scala.annotation.tailrec
import scala.util.Random

object GameOps {

  private val ownedVillains: Set[Villain] = ownedExpansions.flatMap(_.villains)

  def getFirstPlayer(player1: Player, player2: Player): Player = {
    if (getRandom().nextBoolean()) player1 else player2
  }

  def getVillainsRemaining(tournament: String, player: Player): Set[Villain] = {
    val tournamentFile = tournamentFiles.filter(_.getFileName.toString.startsWith(tournament)).head
    val gamesThisTournament = readJsonFile(tournamentFile, Game.tournamentGameDecoder)
    val playersWins = gamesThisTournament.filter(_.winnerPlayer == player).map(_.winner)
    ownedVillains.filterNot(playersWins.contains(_))
  }

  /**
   * @param tournament  The filename prefix containing the ongoing tournament records
   * @return            Some playable game, if one exists - if None, then the tournament is over!
   */
  def getNextGame(tournament: String, player1: Player, player2: Player): Option[PlayableGame] = {
    val player1Villains = getVillainsRemaining(tournament, player1)
    val player2Villains = getVillainsRemaining(tournament, player2)

    getMatch(player1Villains, player2Villains) match {
      case None => None
      case Some(MatchUp(v1, v2)) =>
        val firstPlayer = getFirstPlayer(player1, player2)
        Some(event.PlayableGame(player1, v1, player2, v2, firstPlayer))
    }


  }

}
