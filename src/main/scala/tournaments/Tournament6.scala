package tournaments
import game.{MatchUp, PlayableGame}
import model.Expansion.ownedExpansions
import model.{Player, Villain}
import stats.PlayerRecord
import util.GameOps.getFirstPlayer
import util.MatchUps.getMatch

object Tournament6 extends Tournament {

  override val version: String = "006"

  override val finished: Boolean = players().map(winCount).max >= 25

  override val availableVillains: Set[Villain] = ownedExpansions.flatMap(_.villains)

  private def winCount(player: Player): Int = games().count(_.winnerPlayer == player)

  def getVillainsRemaining(player: Player): Set[Villain] = {
    val playCounts: Map[Villain, Int] = PlayerRecord.getPlayCounts(games(), player)

    if (playCounts.isEmpty) availableVillains else {
      val maxPlayCount = playCounts.values.max // throws on empty map
      availableVillains -- playCounts.filter(kv => kv._2 == maxPlayCount).keys
    }
  }

  override def generateNextGame(player1: Player, player2: Player): Option[PlayableGame] = {
    if (finished) {
      congratulate()
      None
    } else {
      val player1Villains = getVillainsRemaining(player1)
      val player2Villains = getVillainsRemaining(player2)

      getMatch(player1Villains, player2Villains) match {
        case None => None
        case Some(MatchUp(v1, v2)) =>
          val firstPlayer = getFirstPlayer(v1, v2)
          Some(PlayableGame(player1, v1, player2, v2, firstPlayer))
      }
    }
  }

}
