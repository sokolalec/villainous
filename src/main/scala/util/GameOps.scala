package util

import game.{MatchUp, PlayableGame}
import io.Filesystem.{readJsonFile, tournamentFiles}
import model.game.DuelGame
import model.{Player, Villain}
import tournaments.Tournament
import util.MatchUps.getMatch
import util.RNG.getRandom

object GameOps {

  def getFirstPlayer(villain1: Villain, villain2: Villain): Villain = {
    if (getRandom().nextBoolean()) villain1 else villain2
  }

  def getVillainsRemaining(tournament: Tournament, player: Player): Set[Villain] = {
    val tournamentFile = tournamentFiles.filter(_.getFileName.toString.startsWith(tournament.version)).head
    val gamesThisTournament = readJsonFile(tournamentFile, DuelGame.tournamentGameDecoder)
    val playersWins = gamesThisTournament.filter(_.winnerPlayer == player).map(_.winner)
    tournament.availableVillains -- playersWins
  }

  /**
   * @param tournament  The filename prefix containing the ongoing tournament records
   * @return            Some playable game, if one exists - if None, then the tournament is over!
   */
  def getNextGame(tournament: Tournament, player1: Player, player2: Player): Option[PlayableGame] = {
    val player1Villains = getVillainsRemaining(tournament, player1)
    val player2Villains = getVillainsRemaining(tournament, player2)

    getMatch(player1Villains, player2Villains) match {
      case None => None
      case Some(MatchUp(v1, v2)) =>
        val firstPlayer = getFirstPlayer(v1, v2)
        Some(PlayableGame(player1, v1, player2, v2, firstPlayer))
    }
  }

}
