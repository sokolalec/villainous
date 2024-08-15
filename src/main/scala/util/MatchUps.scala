package util

import model.environment.{MatchUp, Villain}
import util.RNG.getRandom

import scala.annotation.tailrec

object MatchUps {

  private def getRandomVillain(villains: Set[Villain]): Option[Villain] = {
    if (villains.isEmpty) None
    else {
      val r = getRandom().nextInt(villains.size)
      villains.view.slice(r, r + 1).headOption
    }
  }

  /**
   * If there is ever a Villain who double fates and cannot be double fated, this would still work.
   */
  private def getPossibleMatches(player1: Villain, player2Pool: Set[Villain]): Set[Villain] = {
    val remaining = if (player1.hasDoubleFate) player2Pool.filterNot(_.impossibleWhenDoubleFated) else player2Pool
    if (player1.impossibleWhenDoubleFated) remaining.filterNot(_.hasDoubleFate) else remaining
  }

  /**
   * Pick a random Villain for player1 from their pool.
   * Filter out any impossible matchups for that Villain from player2's pool.
   * Pick a random Villain for player2 from the remaining options.
   *
   * If there are no valid games with the random Villain you picked for player1, remove that Villain and pick again.
   * If there are no Villains in player1's pool which have any valid matches against player2's pool, return None.
   */
  @tailrec
  def getMatch(player1Pool: Set[Villain], player2Pool: Set[Villain]): Option[MatchUp] = {
    getRandomVillain(player1Pool) match {
      case None => None
      case Some(v1) =>
        val availableVillains = getPossibleMatches(v1, player2Pool)
        val player2Villain = getRandomVillain(availableVillains)

        player2Villain match {
          case Some(v2) => Some(MatchUp(v1, v2))
          case None => getMatch(player1Pool.filterNot(_ == v1), player2Pool)
        }
    }
  }

}
