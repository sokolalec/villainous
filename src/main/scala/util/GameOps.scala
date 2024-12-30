package util

import game.{MatchUp, PlayableDuel, PlayableGame}
import model.Expansion.ownedExpansions
import model.Villain.{Gaston, Hades}
import model.{Player, Villain}
import tournaments.Tournament
import util.MatchUps.{getMatch, getRandomT}
import util.RNG.getRandom

import scala.annotation.tailrec

object GameOps {

  def getFirstPlayer(villain1: Villain, villain2: Villain): Villain = {
    if (getRandom().nextBoolean()) villain1 else villain2
  }

  def getVillainsRemaining(tournament: Tournament, player: Player): Set[Villain] = {
    val playersWins = tournament.games().filter(_.winnerPlayer == player).map(_.winner)
    tournament.availableVillains -- playersWins
  }

  /**
   * @param tournament  The filename prefix containing the ongoing tournament records
   * @return            Some playable game, if one exists - if None, then the tournament is over!
   */
  def getNextGame(tournament: Tournament, player1: Player, player2: Player): Option[PlayableDuel] = {
    val player1Villains = getVillainsRemaining(tournament, player1)
    val player2Villains = getVillainsRemaining(tournament, player2)

    getMatch(player1Villains, player2Villains) match {
      case None => None
      case Some(MatchUp(v1, v2)) =>
        val firstPlayer = getFirstPlayer(v1, v2)
        Some(PlayableDuel(player1, v1, player2, v2, firstPlayer))
    }
  }

  def proposeMultiDuelGame(players: Set[Player], numVillains: Int): Option[PlayableGame] = {
    val villainPool = ownedExpansions.flatMap(_.villains) -- Set(Gaston, Hades)

    @tailrec
    def getVillains(chosen: Seq[Villain], remaining: Set[Villain]): Seq[Villain] =
      if (chosen.size == numVillains) chosen
      else {
        getRandomT(remaining) match {
          case None => chosen
          case Some(v) => getVillains(chosen :+ v, remaining - v)
        }
      }

    @tailrec
    def getPlayerVillains(players: Set[Player], assignments: Map[Player, Seq[Villain]]): Map[Player, Seq[Villain]] = {
      players.headOption match {
        case None => assignments
        case Some(p) =>
          val availableVillains = villainPool -- assignments.flatMap(_._2)
          getPlayerVillains(players - p, assignments + (p -> getVillains(Seq.empty, availableVillains)))
      }
    }

    if (villainPool.size < numVillains * players.size) None
    else {
      val assignments: Map[Player, Seq[Villain]] = getPlayerVillains(players, Map.empty)
      val order: Seq[Player] = getRandom().shuffle(players.toSeq)
      Some(PlayableGame(order.map(player => (player, assignments(player)))))
    }
  }

}
